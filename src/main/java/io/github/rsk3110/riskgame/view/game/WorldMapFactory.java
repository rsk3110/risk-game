package io.github.rsk3110.riskgame.view.game;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxStylesheet;
import io.github.rsk3110.riskgame.Territory;
import io.github.rsk3110.riskgame.TerritoryEdge;
import io.github.rsk3110.riskgame.World;
import org.jgrapht.ext.JGraphXAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WorldMapFactory {
    private WorldMapFactory() {}
    
    public static mxGraphComponent makeWorldMap(final World world) {
        final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter = new JGraphXAdapter<Territory, TerritoryEdge>(world.getGraph()) {
            public boolean isCellSelectable(Object cell) {
                return !model.isEdge(cell) && super.isCellSelectable(cell);
            }

            @Override
            public String getLabel(final Object rawCell) {
                final mxCell cell = (mxCell) rawCell;
                if (cell.isVertex()) {
                    final Territory territory = ((Territory) cell.getValue());
                    return territory.getName() + '\n' + territory.getArmies();
                } else {
                    return "";
                }
            }
        };
        configureGraphStylesheet(graphAdapter.getStylesheet());
        configureGraphVertexStyles(graphAdapter);
        configureGraphLayout(graphAdapter);

        final mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter) {
            @Override
            public boolean isPanningEvent(final MouseEvent event) {
                return (event != null) && event.isControlDown();
            }

            @Override
            public boolean isEditEvent(final MouseEvent e) {
                return false;
            }
        };
        configureGraphComponent(graphComponent);

        return graphComponent;
    }

    private static void configureGraphComponent(final mxGraphComponent graphComponent) {
        graphComponent.setPanning(true);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        graphComponent.getGraph().setCellsResizable(false);
        graphComponent.getGraph().setConnectableEdges(false);
        graphComponent.getGraphHandler().setCloneEnabled(false);

        graphComponent.setCenterZoom(true);

        attachMouseWheelZoomListener(graphComponent);
    }

    private static void attachMouseWheelZoomListener(final mxGraphComponent graphComponent) {
        graphComponent.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    graphComponent.zoomIn();
                } else {
                    graphComponent.zoomOut();
                }
            }
        });
    }

    private static void configureGraphStylesheet(final mxStylesheet stylesheet) {
        stylesheet.getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);

        stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
    }

    private static void configureGraphVertexStyles(final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter) {
        graphAdapter.getModel().beginUpdate();
        try {
            graphAdapter.clearSelection();
            graphAdapter.selectAll();

            final Object[] cells = graphAdapter.getSelectionCells();
            configureGraphVertexScale(cells);
            configureGraphVertexStyles(graphAdapter.getModel(), cells);

        } finally {
            graphAdapter.clearSelection();
            graphAdapter.getModel().endUpdate();
        }
    }

    private static void configureGraphLayout(final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter) {
        final mxFastOrganicLayout layout = new mxFastOrganicLayout(graphAdapter);
        layout.setUseInputOrigin(false);
        layout.setInitialTemp(300.0);
        layout.setForceConstant(80.0);
        layout.execute(graphAdapter.getDefaultParent());
    }

    private static void configureGraphVertexScale(final Object[] cells) {
        for (final Object rawCell : cells) {
            final mxCell cell = (mxCell) rawCell;
            final mxGeometry geometry = cell.getGeometry();
            if (cell.isVertex()) {
                geometry.setWidth(40);
                geometry.setHeight(40);
            }
        }
    }

    private static void configureGraphVertexStyles(final mxIGraphModel graphModel, final Object[] cells) {
        Arrays.stream(cells)
                .map(mxCell.class::cast)
                .collect(Collectors.groupingBy(cell -> ((Territory) cell.getValue()).getContinent()))
                .forEach((continent, territoryCells) -> {
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_STROKECOLOR, Integer.toString(continent.getColor().getRGB()));
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_STROKEWIDTH, "5");
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_FONTSIZE, "12");
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_FONTSTYLE, Integer.toString(Font.BOLD));
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_FONTCOLOR, "0x000000");
                });
    }
}
