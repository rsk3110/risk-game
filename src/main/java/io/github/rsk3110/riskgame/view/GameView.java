package io.github.rsk3110.riskgame.view;

import io.github.rsk3110.riskgame.WorldLoader;

import javax.swing.*;
import java.awt.*;

public class GameView {
    private static final Dimension DEFAULT_SIZE = new Dimension(1080, 720);

    private final JFrame root;
    private JPanel currentScreen;

    public GameView(final WorldLoader worldLoader) {
        this.root = new JFrame();

        this.setScreen(new GameStartupScreen(this, worldLoader));

        this.root.setTitle("RISK");
        this.root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.root.setPreferredSize(DEFAULT_SIZE);
        this.root.getContentPane().setPreferredSize(DEFAULT_SIZE);
        this.root.pack();
        this.root.setVisible(true);
    }

    public void setScreen(final JPanel panel) {
        if (this.currentScreen != null) {
            this.root.getContentPane().remove(this.currentScreen);
        }
        this.root.getContentPane().add(panel);
        this.currentScreen = panel;
        this.root.revalidate();
        this.root.repaint();
    }
}
