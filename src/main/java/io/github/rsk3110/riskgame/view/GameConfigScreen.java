package io.github.rsk3110.riskgame.view;

import com.esotericsoftware.tablelayout.swing.Table;
import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.World;
import io.github.rsk3110.riskgame.WorldLoader;
import io.github.rsk3110.riskgame.controller.SimpleGameController;
import io.github.rsk3110.riskgame.view.game.InGameScreen;

import javax.swing.*;
import java.awt.*;

public class GameConfigScreen extends JPanel {
    private final GameView gameScreen;

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

        final JList<Integer> playersList = this.makePlayerCountsList(this.makePlayerCountsListModel());
        final JScrollPane playersListPane = new JScrollPane(playersList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JList<Integer> playersAIList = this.makePlayerCountsList(this.makeAIPlayerCountsListModel());
        final JScrollPane playersAIListPane = new JScrollPane(playersAIList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JButton doneButton = new JButton("Select World");
        doneButton.setFont(new Font("Arial", Font.PLAIN, 24));
        doneButton.addActionListener(e -> {
            if (worldsList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No world has been chosen yet!");
            } else if (playersList.isSelectionEmpty() && playersAIList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this, "No player count has been chosen yet!");
            } else if(playersList.isSelectionEmpty() && !(playersAIList.isSelectionEmpty())){
                this.createGame(worldLoader, worldsList.getSelectedValue(), playersAIList.getSelectedValue(), true);
            } else if(!(playersList.isSelectionEmpty()) && !(playersAIList.isSelectionEmpty())){
                JOptionPane.showMessageDialog(this, "Cannot choose both AI and not AI players!");
            } else {
                this.createGame(worldLoader, worldsList.getSelectedValue(), playersList.getSelectedValue(), false);
            }
        });

        final Table table = new Table();
        table.addCell(worldSelectLabel).pad(10);
        table.addCell(playerConfigLabel).pad(10);
        table.addCell(playerAIConfigLabel).pad(10);
        table.row();
        table.addCell(worldsListPane).fillX().pad(10);
        table.addCell(playersListPane).fillX().top().pad(10);
        table.addCell(playersAIListPane).fillX().top().pad(10);
        table.row();
        table.addCell(doneButton).colspan(2);
        this.add(table, BorderLayout.CENTER);
    }

    private void createGame(final WorldLoader worldLoader, final String worldName, final Integer playerCount, final boolean AI) {
        final World world = worldLoader.load(worldName);
        final Game game = new Game(world, playerCount, AI);

        this.gameScreen.setScreen(new InGameScreen(game));
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

    private ListModel<Integer> makePlayerCountsListModel() {
        final DefaultListModel<Integer> counts = new DefaultListModel<>();
        for (int i = 2; i <= 6; ++i) counts.addElement(i);
        return counts;
    }

    private ListModel<Integer> makeAIPlayerCountsListModel() {
        final DefaultListModel<Integer> counts = new DefaultListModel<>();
        for (int i = 2; i <= 6; ++i) counts.addElement(i);
        return counts;
    }
}
