package io.github.rsk3110.riskgame;

import java.util.List;

public interface Command {
    public boolean execute(Player player);
    public boolean execute(Player player, List<String> args);
}
