package io.github.rsk3110.riskgame;

import java.util.List;

public class SkipCommand implements Command {

    public SkipCommand(){

    }

    public boolean execute(Player player) {
        return true;
    }

    public boolean execute(Player player, List<String> args) {
        System.out.println("Invalid arguments. Skip does not take in any arguments. {skip}");
        return false;
    }
}