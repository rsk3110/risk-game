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


public class InGameScreen extends JPanel {

    private Territory selectedTerritory;

    private final mxGraphComponent graph;

    private final JLabel notificationBox;

    public InGameScreen(final Game game) {
        this.setLayout(new BorderLayout());
        this.notificationBox = new JLabel();
        notificationBox.setFont(new Font("Arial", Font.PLAIN, 24));

        this.graph = WorldMapFactory.makeWorldMap(game.getWorld());
        this.configureScreenComponents(game);

        graph.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, this::onTerritoryClick);
    }

    private void configureScreenComponents(Game game) {
        final JLabel options = new JLabel("Options");
        options.setFont(new Font("Arial", Font.PLAIN, 24));

        final JButton attack = new JButton("Attack");
        attack.setFont(new Font("Arial", Font.PLAIN, 18));
        attack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.getCommandManager().execute("attack");
            }
        });
        final JButton fortify = new JButton("Fortify");
        fortify.setFont(new Font("Arial", Font.PLAIN, 18));
        fortify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.getCommandManager().execute("fortify");
            }
        });
        final JButton skip = new JButton("Skip");
        skip.setFont(new Font("Arial", Font.PLAIN, 18));
        skip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.getCommandManager().execute("skip");
            }
        });
        final JButton quit = new JButton("Quit");
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.getCommandManager().execute("quit");
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

        this.selectedTerritory = (Territory) cell.getValue();
        this.notificationBox.setText("Clicked on " + selectedTerritory.getName());
    }
}
