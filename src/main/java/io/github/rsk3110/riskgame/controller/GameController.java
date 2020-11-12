package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.Territory;

public interface GameController {
    void attack(Territory from, Territory to, int attackingArmies, int defendingArmies);
    void fortify(Territory from, Territory to, int armies);
    void skipTurn();
    void quitGame();
}
