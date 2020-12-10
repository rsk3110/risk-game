package io.github.rsk3110.riskgame.view;

import com.esotericsoftware.tablelayout.swing.Table;
import io.github.rsk3110.riskgame.WorldLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GameStartupScreen extends JPanel {
    private final GameView gameScreen;
    private final transient WorldLoader worldLoader;

    public GameStartupScreen(final GameView gameScreen, final WorldLoader worldLoader) {
        this.gameScreen = gameScreen;
        this.worldLoader = worldLoader;
        this.setLayout(new BorderLayout());
        this.add(this.makeMainMenu(), BorderLayout.CENTER);
    }

    private JComponent makeMainMenu() {
        final JLabel gameLabel = new JLabel("RISK");
        gameLabel.setFont(new Font("Arial Black", Font.PLAIN, 80));

        final JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.addActionListener(e -> {
            this.gameScreen.setScreen(new GameConfigScreen(this.gameScreen, this.worldLoader));
        });

        final List<JLabel> tipList = Arrays.asList(
                new JLabel("Tip: Click on territories to select them"),
                new JLabel("Tip: Hold CTRL and rotate the scrollwheel to zoom in and out"),
                new JLabel("Tip: Hold CTRL to pan without the scrollbars")
        );
        for (final JLabel tip : tipList) {
            tip.setFont(new Font("Arial", Font.ITALIC, 20));
        }

        final Table table = new Table();

        table.addCell(gameLabel).center();
        table.row();
        table.addCell(startButton).center().padBottom(75);
        for (final JLabel tip : tipList) {
            table.row();
            table.addCell(tip);
        }

        return table;
    }
}
