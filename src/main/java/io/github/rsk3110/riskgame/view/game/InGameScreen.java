package io.github.rsk3110.riskgame.view.game;

import com.esotericsoftware.tablelayout.swing.Table;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.Player;
import io.github.rsk3110.riskgame.Territory;
import io.github.rsk3110.riskgame.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


public class InGameScreen extends JPanel {
    private final mxGraphComponent map;
    
    private mxCell selectedCell;
    private ClickMode currMode;

    private final GameController gameController;
    private final JLabel notificationBox;
    
    private final Map<Player, Color> playerColors;

    static private enum ClickMode {
        DEFAULT,
        ATTACK,
        FORTIFY;
    };

    public InGameScreen(final GameController gameController) {
        this.gameController = gameController;
        this.selectedCell = null;
        this.currMode = ClickMode.DEFAULT;
        this.map = WorldMapFactory.makeWorldMap(gameController.getWorld());
        this.notificationBox = new JLabel();
        {
            notificationBox.setFont(new Font("Arial", Font.PLAIN, 24));
        }
        this.playerColors = this.populatePlayerColors(gameController.getGame());

        this.setLayout(new BorderLayout());
        this.configureScreenComponents();

        map.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, this::onTerritoryClick);
        gameController.addTurnStartListener(this::onTurnStart);

        this.populateTerritoryCells(map.getGraph()).forEach((territory, cell) -> {
            territory.addTerritoryChangeListener(t -> this.refreshTerritory(t, cell));
        });

        gameController.init();
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
        List<Integer> colors = new ArrayList<>();
        int color;

        for (final Player p : game.getPlayers()) {
            do {
                color = random.nextInt();
            } while(!colors.contains(color));
            colors.add(color);
            final float hue = color;
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

    private void updateName(JLabel name) {
        name.setText("Current Player: " + gameController.getCurrPlayer().getName());
    }

    private void configureScreenComponents() {
        JLabel name = new JLabel("a");
        name.setFont(new Font("Arial", Font.PLAIN, 24));
        updateName(name);

        final JLabel options = new JLabel("Options");
        options.setFont(new Font("Arial", Font.PLAIN, 24));

        final JButton attack = new JButton("Attack");
        attack.setFont(new Font("Arial", Font.PLAIN, 18));
        attack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(selectedCell == null) return;
                notificationBox.setText("Select The Territory To Attack");
                currMode = ClickMode.ATTACK;
                updateName(name);
                attack.setText("Attacking...");
            }
        });
        final JButton fortify = new JButton("Fortify");
        fortify.setFont(new Font("Arial", Font.PLAIN, 18));
        fortify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(selectedCell == null) return;
                notificationBox.setText("Select The Territory To Fortify");
                currMode = ClickMode.FORTIFY;
                updateName(name);
            }
        });
        final JButton skip = new JButton("Skip");
        skip.setFont(new Font("Arial", Font.PLAIN, 18));
        skip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameController.getCommandManager().execute("skip");
                updateName(name);
            }
        });
        final JButton quit = new JButton("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameController.getCommandManager().execute("quit");
                updateName(name);
            }
        });

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
        table.addCell(name);
        table.row();
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
        Territory origin = selectedCell != null ? (Territory)selectedCell.getValue() : (Territory)cell.getValue();
        Territory target = (Territory)cell.getValue();

        switch(currMode) {
            case ATTACK: {
                this.notificationBox.setText("Attacking " + target.getName() + "From " +  origin.getName());
                gameController.getCommandManager().execute("attack", new ArrayList<String>() {{ // get dice amounts
                    add(origin.getName());
                    add(target.getName());
                }});
                currMode = ClickMode.DEFAULT;

                break;
            }
            case FORTIFY: {
                this.notificationBox.setText("Fortifying " + target.getName() + "Using " +  origin.getName());
                gameController.getCommandManager().execute("fortify", new ArrayList<String>() {{ // get amount of armies
                    add(origin.getName());
                    add(target.getName());
                    add(JOptionPane.showInputDialog("How many armies would you like to fortify with?"));
                }});
                currMode = ClickMode.DEFAULT;

                break;
            }
            default: {
                this.selectedCell = cell;
                this.notificationBox.setText("Clicked on " + ((Territory)selectedCell.getValue()).getName());
                break;
            }
        }
    }
}
