package io.github.rsk3110.riskgame.view.game;

import com.esotericsoftware.tablelayout.swing.Table;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraphSelectionModel;
import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.Territory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InGameScreen extends JPanel {

    private mxCell selectedCell;
    private ClickMode currMode;

    private final Game game;
    private final mxGraphComponent graph;
    private final JLabel notificationBox;

    static private enum ClickMode {
        DEFAULT,
        ATTACK,
        FORTIFY;
    };

    public InGameScreen(final Game game) {
        this.game = game;
        this.selectedCell = null;
        this.currMode = ClickMode.DEFAULT;

        this.setLayout(new BorderLayout());
        this.notificationBox = new JLabel();
        notificationBox.setFont(new Font("Arial", Font.PLAIN, 24));

        this.graph = WorldMapFactory.makeWorldMap(game.getWorld());
        this.graph.getGraph().setCellsMovable(false);
        this.configureScreenComponents();

        graph.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, this::onTerritoryClick);
    }

    private void updateName(JLabel name) {
        name.setText("Current Player: " + game.getCurrPlayer().getName());
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
                game.getCommandManager().execute("skip");
                updateName(name);
            }
        });
        final JButton quit = new JButton("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.getCommandManager().execute("quit");
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
        table.addCell(graph).height(640).fill();
        table.addCell(optionsTable);
        table.row();
        table.addCell(this.notificationBox).height(30).colspan(2).center();
        this.add(table);
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
                game.getCommandManager().execute("attack", new ArrayList<String>() {{ // get dice amounts
                    add(origin.getName());
                    add(target.getName());
                }});
                currMode = ClickMode.DEFAULT;

                break;
            }
            case FORTIFY: {this.notificationBox.setText("Fortifying " + target.getName() + "Using " +  origin.getName());
                game.getCommandManager().execute("fortify", new ArrayList<String>() {{ // get amount of armies
                    add(origin.getName());
                    add(target.getName());
                    add("5");
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
