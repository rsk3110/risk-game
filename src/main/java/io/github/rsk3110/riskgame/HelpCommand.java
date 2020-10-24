package io.github.rsk3110.riskgame;

import java.util.List;

public class HelpCommand implements Command {

    public HelpCommand(){

    }

    public boolean execute(Player player) {
        return false;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
