package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.*;

import java.util.function.Consumer;

/**
 * Interface for GameController class
 *
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 */
public interface GameController {
    boolean attack(Territory from, Territory to, int attackingArmies, int defendingArmies);
    boolean fortify(Territory from, Territory to, int armies);
    void skipTurn();
    void quitGame();
    void init();
    void addTurnStartListener(Consumer<Player> onTurnStart);
    void allocateBonusArmies(Territory target);
    Player getCurrPlayer();
    Game getGame();
    World getWorld();
}
