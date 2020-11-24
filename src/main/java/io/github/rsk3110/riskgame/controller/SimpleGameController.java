package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.*;

import java.util.function.Consumer;

public class SimpleGameController implements GameController {

    private final Game game;

    public SimpleGameController(final Game game) {
        this.game = game;
    }

    @Override
    public void attack(Territory from, Territory to, int attackingArmies, int defendingArmies) {

    }

    @Override
    public void fortify(Territory from, Territory to, int armies) {

    }

    @Override
    public void skipTurn() {
        this.game.nextTurn();
    }

    @Override
    public void quitGame() {
        this.game.quitGame();
    }

    @Override
    public void init() {
        this.game.init();
    }

    @Override
    public void addTurnStartListener(Consumer<Player> onTurnStart) {
        this.game.addTurnStartListener(onTurnStart);
    }

    @Override
    public Player getCurrPlayer() {
        return this.game.getCurrPlayer();
    }

    @Override
    public CommandManager getCommandManager() {
        return this.game.getCommandManager();
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public World getWorld() {
        return this.game.getWorld();
    }
}
