package io.github.rsk3110.riskgame;

import java.util.List;

public class SkipCommand implements Command {

    public SkipCommand(){

    }

    public boolean execute(Player player) {
        return true;
    }

    public boolean execute(Player player, List<String> args) {
        return true;
    }
}