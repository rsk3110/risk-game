package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Quits game. Takes in no arguments.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class QuitCommand implements Command {

    public QuitCommand() {
    }

    /**
     * Simple method to quit game.
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        System.out.println("The game is over.");
        System.exit(0);
        return false;
    }

    /**
     * Quit cannot be executed with any arguments.
     *
     * @param player player executing the command
     * @param args arguments of execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        System.out.println("Invalid arguments. Quit does not take in any arguments. {quit}");
        return false;
    }
}