package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Interface that implement execute as a common method in each command class
 *
 * @author Kaue Gomes e Sousa de Oliveira
 **/
public interface Command {
    public boolean execute(Player player);
    public boolean execute(Player player, List<String> args);
}
