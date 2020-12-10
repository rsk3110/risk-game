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
import io.github.rsk3110.riskgame.*;
import io.github.rsk3110.riskgame.controller.GameController;
import javafx.util.Pair;

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
        FORTIFY,
        BONUS,
    };

    public InGameScreen(final GameController gameController) {
        this.gameController = gameController;
        this.selectedCell = null;
        this.currMode = ClickMode.DEFAULT;
        this.map = WorldMapFactory.makeWorldMap(gameController.getWorld());
        this.notificationBox = new JLabel();
        notificationBox.setFont(new Font("Arial", Font.PLAIN, 24));

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
                color = Math.abs(random.nextInt());
            } while(colors.contains(color));
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

    /**
     * Updates current player name
     * @param name current player name
     */
    private void updateName(JLabel name) {
        name.setText("Current Player: " + gameController.getCurrPlayer().getName());
    }

    /**
     * Configures view components
     */
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
                if(!(currMode == ClickMode.DEFAULT) || selectedCell == null) return;
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
                if(!(currMode == ClickMode.DEFAULT) || selectedCell == null) return;
                notificationBox.setText("Select The Territory To Fortify");
                currMode = ClickMode.FORTIFY;
                updateName(name);
            }
        });

        final JButton save = new JButton("Save");
        save.setFont(new Font("Arial", Font.PLAIN, 18));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!(currMode == ClickMode.DEFAULT)) return;
                try {
                    gameController.save();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                updateName(name);
            }
        });

        final JButton skip = new JButton("Skip");
        skip.setFont(new Font("Arial", Font.PLAIN, 18));
        skip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!(currMode == ClickMode.DEFAULT)) return;
                gameController.skipTurn();
                updateName(name);
            }
        });

        final JButton quit = new JButton("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameController.quitGame();
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

    private void onTurnStart(final Pair<Player, Integer> turnPair) {
        Player player = turnPair.getKey();
        int currRound = turnPair.getValue();

        this.notificationBox.setText(String.format("%s's Turn!", player.getName()));
        if(currRound > 0) {
            if(player.getClass() != AI.class) {
                setBonusArmiesPhase(player);
            } else {
                setBonusArmiesAI((AI)player);
            }
        }
    }

    /**
     * Sets bonus army status and prompts player
     * @param p current player
     */
    private void setBonusArmiesPhase(Player p) {
        Set<Continent> occupied = p.getOccupiedContinents();
        StringBuilder occupiedString = new StringBuilder("");
        for(Continent c : occupied) {
            occupiedString.append(c + ", ");
        }
        String prompt = "By controlling " + p.getTerritories().size() + " territories" +
                ((p.getOccupiedContinents().size() != 0) ? ("\nand the continents " + occupiedString.substring(0, occupiedString.length() - 3)) : "") +
                "\nyou now hold " + p.getArmies() + " armies to allocate." +
                "\nPick where to move them.";
        JOptionPane.showMessageDialog(null, prompt);
        notificationBox.setText("Allocating " + p.getArmies() + " armies.");
        currMode = ClickMode.BONUS;
    }

    /**
     * Assigns bonus armies to AI territory
     * @param ai current ai
     */
    private void setBonusArmiesAI(AI ai) {
        List<Territory> occupied = ai.getTerritories();
        Collections.shuffle(occupied);
        gameController.allocateBonusArmies(occupied.get(0), ai.getArmies());
    }

    /**
     * Callback function for clicking on territories.
     * @param sender event sender
     * @param evt event
     */
    private void onTerritoryClick(final Object sender, final mxEventObject evt) {
        final mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
        final mxCell cell = (mxCell) sm.getCell();
        if (cell == null || cell.isEdge()) return;
        Territory origin = selectedCell != null ? (Territory)selectedCell.getValue() : (Territory)cell.getValue();
        Territory target = (Territory)cell.getValue();
        Player currPlayer = gameController.getCurrPlayer();

        switch(currMode) {
            case ATTACK: {
                notificationBox.setText("Attacking " + target.getName() + "From " +  origin.getName());
                gameController.attack(origin, target,
                        Helper.promptForIntegerValue(
                            1,
                            Math.min(3, origin.getArmies()),
                            "How many dice would you like to roll?"),
                        Helper.promptForIntegerValue(
                            1,
                            Math.min(2, target.getArmies()),
                            target.getOccupant().getName() + ": How many dice would you like to roll?"));
                currMode = ClickMode.DEFAULT;
                selectedCell = null;

                break;
            }
            case FORTIFY: {
                notificationBox.setText("Fortifying " + target.getName() + "Using " +  origin.getName());
                if(gameController.fortify(origin, target, Helper.promptForIntegerValue(
                        1,
                        gameController.getGame().getCurrPlayer().getArmies(),
                        "How many armies would you like to fortify with?"))) gameController.skipTurn();
                currMode = ClickMode.DEFAULT;
                selectedCell = null;

                break;
            }
            case BONUS: {
                int playerArmyCount = gameController.getCurrPlayer().getArmies();
                gameController.allocateBonusArmies(target, Helper.promptForIntegerValue(
                        1,
                        playerArmyCount,
                        "How many armies would you like to allocate?"));
                playerArmyCount = gameController.getCurrPlayer().getArmies();
                notificationBox.setText("Allocating " + playerArmyCount + " armies.");
                if(playerArmyCount == 0) currMode = ClickMode.DEFAULT;
                this.selectedCell = null;

                break;
            }
            default: {
                selectedCell = cell;
                notificationBox.setText("Clicked on " + ((Territory)selectedCell.getValue()).getName());
                break;
            }
        }
    }
}
