package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.*;
import javafx.util.Pair;

import java.util.function.Consumer;

/**
 * Implementation of GameController.
 *
 * @author Mark Johnson
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class SimpleGameController implements GameController {

    private final Game game;

    /**
     * Initializes GameController
     * @param game game to initialize with
     */
    public SimpleGameController(final Game game) {
        this.game = game;
        game.setGameController(this);
    }

    /**
     * Executes attack command
     * @param from territory attacking
     * @param to territory being attack
     * @param attackingArmies number of armies attacking
     * @param defendingArmies number of armies defending
     * @return whether the command was successful
     */
    @Override
    public boolean attack(Territory from, Territory to, int attackingArmies, int defendingArmies) {
        return AttackCommand.execute(getCurrPlayer(), from, to, attackingArmies, defendingArmies);
    }

    /**
     * Executes fortify command
     * @param from territory to fortify with
     * @param to territory to fortify
     * @param armies number of armies to fortify with
     * @return whether the command was successful
     */
    @Override
    public boolean fortify(Territory from, Territory to, int armies) {
        return FortifyCommand.execute(getCurrPlayer(), from, to, armies);
    }

    /**
     * Skips to next turn
     */
    @Override
    public void skipTurn() {
        game.nextTurn();
    }

    /**
     * Quits the game
     */
    @Override
    public void quitGame() {
        this.game.quitGame();
    }

    /**
     * Initializes game board
     */
    @Override
    public void init() {
        game.init();
    }

    /**
     * Adds a turn listener to game
     * @param onTurnStart game listener
     */
    @Override
    public void addTurnStartListener(Consumer<Pair<Player, Integer>> onTurnStart) {
        game.addTurnStartListener(onTurnStart);
    }

    /**
     * Allocates bonus armies to target territory
     * @param target territory to allocate to
     */
    @Override
    public void allocateBonusArmies(Territory target, int count) {
        game.allocateBonusArmies(target, count);
    }

    /**
     * Gets current player
     * @return player who's turn it is
     */
    @Override
    public Player getCurrPlayer() {
        return game.getCurrPlayer();
    }

    /**
     * Gets game
     * @return Game object
     */
    @Override
    public Game getGame() {
        return game;
    }

    /**
     * Gets current world
     * @return World object
     */
    @Override
    public World getWorld() {
        return game.getWorld();
    }
}
