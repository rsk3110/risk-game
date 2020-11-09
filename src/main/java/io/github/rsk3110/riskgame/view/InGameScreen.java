package io.github.rsk3110.riskgame.view;

import com.esotericsoftware.tablelayout.swing.Table;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxStylesheet;
import io.github.rsk3110.riskgame.*;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.Collectors;


public class InGameScreen extends JPanel {

    private final World world;

    public InGameScreen(final World world) {
        this.world = world;
        this.setLayout(new BorderLayout());

        final mxGraphComponent graph = this.makeGraphGUI();

        final Table table = new Table();
        table.addCell(graph).height(640).fill();

        this.add(table);
    }

    private mxGraphComponent makeGraphGUI() {
        final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter = new JGraphXAdapter<Territory, TerritoryEdge>(this.world.getGraph()) {
            public boolean isCellSelectable(Object cell) {
                return !model.isEdge(cell) && super.isCellSelectable(cell);
            }

            @Override
            public String getLabel(final Object cell) {
                return "";
            }
        };
        this.configureGraphStylesheet(graphAdapter.getStylesheet());
        this.configureGraphVertexStyles(graphAdapter);
        this.configureGraphLayout(graphAdapter);

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
        this.configureGraphComponent(graphComponent);

        return graphComponent;
    }

    private void configureGraphComponent(final mxGraphComponent graphComponent) {
        graphComponent.setPanning(true);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        graphComponent.getGraph().setCellsResizable(false);
        graphComponent.getGraph().setConnectableEdges(false);
        graphComponent.getGraphHandler().setCloneEnabled(false);

        graphComponent.setCenterZoom(true);

        this.attachMouseWheelZoomListener(graphComponent);
    }

    private void attachMouseWheelZoomListener(final mxGraphComponent graphComponent) {
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

    private void configureGraphStylesheet(final mxStylesheet stylesheet) {
        stylesheet.getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        stylesheet.getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);

        stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
    }

    private void configureGraphVertexStyles(final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter) {
        graphAdapter.getModel().beginUpdate();
        try {
            graphAdapter.clearSelection();
            graphAdapter.selectAll();

            final Object[] cells = graphAdapter.getSelectionCells();
            this.configureGraphVertexScale(cells);
            this.configureGraphVertexStyles(graphAdapter.getModel(), cells);

        } finally {
            graphAdapter.clearSelection();
            graphAdapter.getModel().endUpdate();
        }
    }

    private void configureGraphLayout(final JGraphXAdapter<Territory, TerritoryEdge> graphAdapter) {
        final mxFastOrganicLayout layout = new mxFastOrganicLayout(graphAdapter);
        layout.setInitialTemp(300.0);
        layout.setForceConstant(70.0);
        layout.execute(graphAdapter.getDefaultParent());
    }

    private void configureGraphVertexScale(final Object[] cells) {
        for (final Object rawCell : cells) {
            final mxCell cell = (mxCell) rawCell;
            final mxGeometry geometry = cell.getGeometry();
            if (cell.isVertex()) {
                geometry.setWidth(40);
                geometry.setHeight(40);
            }
        }
    }

    private void configureGraphVertexStyles(final mxIGraphModel graphModel, final Object[] cells) {
        Arrays.stream(cells)
                .map(mxCell.class::cast)
                .collect(Collectors.groupingBy(cell -> ((Territory) cell.getValue()).getContinent()))
                .forEach((continent, territoryCells) -> {
                    for (final mxCell c : territoryCells) {
                        graphModel.setValue(c, "");
                    }
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_STROKECOLOR, Integer.toString(continent.getColor().getRGB()));
                    mxStyleUtils.setCellStyles(graphModel, territoryCells.toArray(), mxConstants.STYLE_STROKEWIDTH, "5");
                });
    }
}
