package io.github.rsk3110.riskgame;

import java.util.List;

public class MapCommand implements Command {

    public MapCommand(){
    }

    public boolean execute(Player player) {
        for(Continent continent : player.getWorld().getContinents()) {
            System.out.println(continent);
        }
        return false;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
