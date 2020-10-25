package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Skips current turn. Takes in no arguments.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class SkipCommand implements Command {

    public SkipCommand(){

    }

    /**
     * Simple method to return true.
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        return true;
    }

    /**
     * Skip cannot be executed with any arguments.
     *
     * @param player player executing the command
     * @param args arguments of execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        System.out.println("Invalid arguments. Skip does not take in any arguments. {skip}");
        return false;
    }
}