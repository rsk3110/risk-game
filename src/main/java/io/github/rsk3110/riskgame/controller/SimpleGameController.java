package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.Territory;

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
        this.game.advanceTurn();
    }

    @Override
    public void quitGame() {
        this.game.quitGame();
    }
}
