package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.*;

import java.util.function.Consumer;

public interface GameController {
    void attack(Territory from, Territory to, int attackingArmies, int defendingArmies);
    void fortify(Territory from, Territory to, int armies);
    void skipTurn();
    void quitGame();
    void init();
    void addTurnStartListener(Consumer<Player> onTurnStart);
    Player getCurrPlayer();
    CommandManager getCommandManager();
    Game getGame();
    World getWorld();
}
