package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.Game;
import io.github.rsk3110.riskgame.Player;
import io.github.rsk3110.riskgame.Territory;
import io.github.rsk3110.riskgame.World;
import javafx.util.Pair;

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
    void addTurnStartListener(Consumer<Pair<Player, Integer>> onTurnStart);
    void allocateBonusArmies(Territory target, int count);
    Player getCurrPlayer();
    Game getGame();
    World getWorld();
}
