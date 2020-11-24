package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.*;

import java.util.function.Consumer;

public class SimpleGameController implements GameController {

    private final Game game;

    public SimpleGameController(final Game game) {
        this.game = game;
    }

    @Override
    public boolean attack(Territory from, Territory to, int attackingArmies, int defendingArmies) {
        return AttackCommand.execute(getCurrPlayer(), from, to, attackingArmies, defendingArmies);
    }

    @Override
    public boolean fortify(Territory from, Territory to, int armies) {
        return FortifyCommand.execute(getCurrPlayer(), from, to, armies);
    }

    @Override
    public void skipTurn() {
        game.nextTurn();
    }

    @Override
    public void quitGame() {
        this.game.quitGame();
    }

    @Override
    public void init() {
        game.init();
    }

    @Override
    public void addTurnStartListener(Consumer<Player> onTurnStart) {
        game.addTurnStartListener(onTurnStart);
    }

    @Override
    public void allocateBonusArmies(Territory target) {
        game.allocateBonusArmies(target);
    }

    @Override
    public Player getCurrPlayer() {
        return game.getCurrPlayer();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public World getWorld() {
        return game.getWorld();
    }
}
