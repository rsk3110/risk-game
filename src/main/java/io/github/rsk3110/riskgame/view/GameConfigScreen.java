package io.github.rsk3110.riskgame.view;

import com.esotericsoftware.tablelayout.swing.Table;
import io.github.rsk3110.riskgame.World;
import io.github.rsk3110.riskgame.WorldLoader;

import javax.swing.*;
import java.awt.*;

public class GameConfigScreen extends JPanel {
    private final GameView gameScreen;
    private final WorldLoader worldLoader;

    public GameConfigScreen(final GameView gameScreen, final WorldLoader worldLoader) {
        this.gameScreen = gameScreen;
        this.worldLoader = worldLoader;
        this.setLayout(new BorderLayout());

        final JLabel header = new JLabel("Configure Your Game");
        header.setFont(new Font("Arial", Font.PLAIN, 36));
        header.setHorizontalAlignment(JLabel.CENTER);
        this.add(header, BorderLayout.NORTH);

        final JLabel worldSelectLabel = new JLabel("World Select");
        worldSelectLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        final JLabel playerConfigLabel = new JLabel("Choose Players");
        playerConfigLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        final JList<String> worldsList = this.makeWorldsList(this.makeWorldsListModel());
        final JScrollPane worldsListPane = new JScrollPane(worldsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JList<Integer> playersList = this.makePlayerCountsList(this.makePlayerCountsListModel());
        final JScrollPane playersListPane = new JScrollPane(playersList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JButton doneButton = new JButton("Select World");
        doneButton.setFont(new Font("Arial", Font.PLAIN, 24));
        doneButton.addActionListener(e -> {
            if (worldsList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No world has been chosen yet!");
            } else if (playersList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No player count has been chosen yet!");
            } else {
                this.loadWorld(worldsList.getSelectedValue(), playersList.getSelectedValue());
            }
        });

        final Table table = new Table();
        table.addCell(worldSelectLabel).pad(10);
        table.addCell(playerConfigLabel).pad(10);
        table.row();
        table.addCell(worldsListPane).fillX().pad(10);
        table.addCell(playersListPane).fillX().top().pad(10);
        table.row();
        table.addCell(doneButton).colspan(2);
        this.add(table, BorderLayout.CENTER);
    }

    private void loadWorld(final String worldName, final Integer playerCount) {
        final World world = this.worldLoader.load(worldName);
        this.gameScreen.setScreen(new InGameScreen(world));
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

    private ListModel<String> makeWorldsListModel() {
        final DefaultListModel<String> worlds = new DefaultListModel<>();
        for (final String world : this.worldLoader.getWorlds()) {
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
        return countsComponent;
    }

    private ListModel<Integer> makePlayerCountsListModel() {
        final DefaultListModel<Integer> counts = new DefaultListModel<>();
        for (int i = 2; i <= 6; ++i) counts.addElement(i);
        return counts;
    }
}
