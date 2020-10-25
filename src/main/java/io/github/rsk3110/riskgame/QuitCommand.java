package io.github.rsk3110.riskgame;

import java.util.List;

public class QuitCommand implements Command {

    Player player;

    public QuitCommand() {
    }

    public boolean execute(Player player) {
        System.out.println("The game is over.");
        System.exit(0);
        return false;
    }

    public boolean execute(Player player, List<String> args) {
        System.out.println("Invalid arguments. Quit does not take in any arguments. {quit}");
        return false;
    }
}