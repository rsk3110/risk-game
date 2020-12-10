package io.github.rsk3110.riskgame.view;

import com.esotericsoftware.tablelayout.swing.Table;
import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.World;
import io.github.rsk3110.riskgame.WorldLoader;
import io.github.rsk3110.riskgame.controller.SimpleGameController;
import io.github.rsk3110.riskgame.view.game.InGameScreen;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class GameConfigScreen extends JPanel {
    private final GameView gameScreen;
    private Table table;
    private JList<Integer> playersList;
    private JList<Integer> aiList;
    private JScrollPane aiPane;

    public GameConfigScreen(final GameView gameScreen, final WorldLoader worldLoader) {
        this.gameScreen = gameScreen;
        this.setLayout(new BorderLayout());

        final JLabel header = new JLabel("Configure Your Game");
        header.setFont(new Font("Arial", Font.PLAIN, 36));
        header.setHorizontalAlignment(JLabel.CENTER);
        this.add(header, BorderLayout.NORTH);

        final JLabel worldSelectLabel = new JLabel("World Select");
        worldSelectLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        final JLabel playerConfigLabel = new JLabel("Choose Players");
        playerConfigLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        final JLabel playerAIConfigLabel = new JLabel("Choose AI Players");
        playerAIConfigLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        final JList<String> worldsList = this.makeWorldsList(this.makeWorldsListModel(worldLoader));
        final JScrollPane worldsListPane = new JScrollPane(worldsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        playersList = this.makePlayerCountsList(this.makePlayerCountsListModel(2, 6));
        final JScrollPane playersListPane = new JScrollPane(this.playersList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.aiList = this.makeAIPlayerCountsList(this.makePlayerCountsListModel(1, 0));
        this.aiPane = new JScrollPane(this.aiList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JButton doneButton = new JButton("Select World");
        doneButton.setFont(new Font("Arial", Font.PLAIN, 24));
        doneButton.addActionListener(e -> {
            if (worldsList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No world has been chosen yet!");
            } else if (this.playersList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No player count has been chosen yet!");
            } else {
                this.createGame(worldLoader, worldsList.getSelectedValue(), this.playersList.getSelectedValue(), (aiList.isSelectionEmpty()) ? 0 : aiList.getSelectedValue());
            }
        });

        table = new Table();
        table.addCell(worldSelectLabel).pad(10);
        table.addCell(playerConfigLabel).pad(10);
        table.addCell(playerAIConfigLabel).pad(10);
        table.row();
        table.addCell(worldsListPane).fillX().pad(10);
        table.addCell(playersListPane).fillX().top().pad(10);
        table.addCell(aiPane).fillX().top().pad(10);
        table.row();
        table.addCell(doneButton).colspan(3);
        this.add(table, BorderLayout.CENTER);
    }

    private void createGame(final WorldLoader worldLoader, final String worldName, final int playerCount, final int AI) {
        final World world = worldLoader.load(worldName);
        final Game game = new Game(world, playerCount, AI, false);

        this.gameScreen.setScreen(new InGameScreen(new SimpleGameController(game)));
    }

    private JList<String> makeWorldsList(final ListModel<String> worlds) {
        final JList<String> worldsComponent = new JList<>(worlds);
        worldsComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        worldsComponent.setVisibleRowCount(5);
        worldsComponent.setFixedCellHeight(50);
        worldsComponent.setFont(new Font("Arial", Font.PLAIN, 24));
        worldsComponent.setBackground(Color.decode("0xd2dae2"));
        return worldsComponent;
    }

    private ListModel<String> makeWorldsListModel(final WorldLoader worldLoader) {
        final DefaultListModel<String> worlds = new DefaultListModel<>();
        for (final String world : worldLoader.getWorlds()) {
            worlds.addElement(world);
        }
        return worlds;
    }

    private JList<Integer> makePlayerCountsList(final ListModel<Integer> playerCounts) {
        final JList<Integer> countsComponent = new JList<>(playerCounts);
        countsComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        countsComponent.setVisibleRowCount(5);
        countsComponent.setFixedCellHeight(35);
        countsComponent.setFont(new Font("Arial", Font.PLAIN, 24));
        countsComponent.setBackground(Color.decode("0xd2dae2"));
        countsComponent.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    int v = playersList.getSelectedValue();
                    aiList.setModel(makePlayerCountsListModel(1, v - 1));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            RepaintManager repaintManager = RepaintManager.currentManager(aiPane);
                            repaintManager.markCompletelyDirty(aiPane);
                            repaintManager.paintDirtyRegions();
                        }
                    });
                }
            }
        });
        return countsComponent;
    }

    private JList<Integer> makeAIPlayerCountsList(final ListModel<Integer> playerCounts) {
        final JList<Integer> countsComponent = new JList<>(playerCounts);
        countsComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        countsComponent.setVisibleRowCount(5);
        countsComponent.setFixedCellHeight(35);
        countsComponent.setFont(new Font("Arial", Font.PLAIN, 24));
        countsComponent.setBackground(Color.decode("0xd2dae2"));

        return countsComponent;
    }

    private ListModel<Integer> makePlayerCountsListModel(int min, int max) {
        final DefaultListModel<Integer> counts = new DefaultListModel<>();
        for (int i = min; i <= max; ++i) counts.addElement(i);
        return counts;
    }
}
