package io.github.rsk3110.riskgame.view;

import io.github.rsk3110.riskgame.WorldLoader;

import javax.swing.*;
import java.awt.*;

public class GameStartupScreen extends JPanel {
    private final GameView gameScreen;
    private final transient WorldLoader worldLoader;

    public GameStartupScreen(final GameView gameScreen, final WorldLoader worldLoader) {
        this.gameScreen = gameScreen;
        this.worldLoader = worldLoader;
        this.setLayout(new BorderLayout());
        this.add(this.makeMainMenu(), BorderLayout.CENTER);
    }

    private JPanel makeMainMenu() {
        final JPanel panel = new JPanel();

        final JLabel gameLabel = new JLabel("RISK");
        gameLabel.setFont(new Font("Arial Black", Font.PLAIN, 80));

        final JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.addActionListener(e -> {
            this.gameScreen.setScreen(new GameConfigScreen(this.gameScreen, this.worldLoader));
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        panel.add(startButton, c);
        panel.add(gameLabel, new GridBagConstraints());
        return panel;
    }
}
