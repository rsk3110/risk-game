package io.github.rsk3110.riskgame.view.game;

import com.esotericsoftware.tablelayout.swing.Table;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
import io.github.rsk3110.riskgame.*;
import io.github.rsk3110.riskgame.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


public class InGameScreen extends JPanel {

    private final GameController ctl;

    private final mxGraphComponent map;

    private final JLabel notificationBox;

    private final Map<Player, Color> playerColors;

    public InGameScreen(final Game game, final GameController ctl) {
        this.ctl = ctl;
        this.map = WorldMapFactory.makeWorldMap(game.getWorld());
        this.notificationBox = new JLabel();
        {
            notificationBox.setFont(new Font("Arial", Font.PLAIN, 24));
        }
        this.playerColors = this.populatePlayerColors(game);

        this.setLayout(new BorderLayout());
        this.configureScreenComponents();

        map.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, this::onTerritoryClick);
        game.addTurnStartListener(this::onTurnStart);

        this.populateTerritoryCells(map.getGraph()).forEach((territory, cell) -> {
            territory.addTerritoryChangeListener(t -> this.refreshTerritory(t, cell));
        });

        game.startGame();
    }

    private Map<Territory, mxCell> populateTerritoryCells(final mxGraph graph) {
        try {
            graph.clearSelection();
            graph.selectAll();

            return Arrays.stream(graph.getSelectionCells())
                    .map(rawCell -> (mxCell) rawCell)
                    .filter(mxCell::isVertex)
                    .collect(Collectors.toMap(cell -> ((Territory) cell.getValue()), cell -> cell));

        } finally {
            graph.clearSelection();
        }
    }

    private Map<Player, Color> populatePlayerColors(final Game game) {
        final Map<Player, Color> playerColorMap = new HashMap<>();
        final Random random = new Random();

        for (final Player p : game.getPlayers()) {
            final float hue = random.nextFloat();
            final float saturation = (random.nextInt(2000) + 1000) / 10000f;
            final float luminance = 0.9f;
            playerColorMap.put(p, Color.getHSBColor(hue, saturation, luminance));
        }
        playerColorMap.put(null, Color.decode("0xADD8E6"));
        return playerColorMap;
    }

    private void refreshTerritory(final Territory territory, final mxCell cell) {
        final mxGraph graph = this.map.getGraph();
        graph.getModel().beginUpdate();
        try {
            mxStyleUtils.setCellStyles(graph.getModel(), new Object[]{cell}, mxConstants.STYLE_FILLCOLOR, Integer.toString(this.playerColors.get(territory.getOccupant()).getRGB()));
            graph.getView().updateLabel(graph.getView().getState(cell));
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void configureScreenComponents() {
        final JLabel options = new JLabel("Options");
        options.setFont(new Font("Arial", Font.PLAIN, 24));

        final JButton attack = new JButton("Attack");
        attack.setFont(new Font("Arial", Font.PLAIN, 18));

        final JButton fortify = new JButton("Fortify");
        fortify.setFont(new Font("Arial", Font.PLAIN, 18));

        final JButton skip = new JButton("Skip");
        skip.setFont(new Font("Arial", Font.PLAIN, 18));
        skip.addActionListener(e -> this.ctl.skipTurn());

        final JButton quit = new JButton("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.addActionListener(e -> this.ctl.quitGame());

        final Table optionsTable = new Table();
        optionsTable.addCell(options);
        optionsTable.row();
        optionsTable.addCell(attack).top().pad(5);
        optionsTable.row();
        optionsTable.addCell(fortify).top().pad(5);
        optionsTable.row();
        optionsTable.addCell(skip).top().pad(5);
        optionsTable.row();
        optionsTable.addCell(quit).top().pad(5);

        final Table table = new Table();
        table.addCell(map).height(640).fill();
        table.addCell(optionsTable);
        table.row();
        table.addCell(this.notificationBox).height(30).colspan(2).center();
        this.add(table);
    }

    private void onTurnStart(final Player p) {
        this.notificationBox.setText(String.format("%s's Turn!", p.getName()));
    }

    private void onTerritoryClick(final Object sender, final mxEventObject evt) {
        final mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
        final mxCell cell = (mxCell) sm.getCell();
        if (cell == null || cell.isEdge()) return;

        this.notificationBox.setText("Clicked on " + ((Territory) cell.getValue()).getName());
    }
}
