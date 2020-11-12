package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Interface for loading worlds
 *
 * @author Mark Johnson
 */
public interface WorldLoader {
    World load(String name);

    List<String> getWorlds();
}
