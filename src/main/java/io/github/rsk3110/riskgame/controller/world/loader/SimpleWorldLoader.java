package io.github.rsk3110.riskgame.controller.world.loader;

import io.github.rsk3110.riskgame.model.world.World;

import java.util.HashMap;
import java.util.Map;

public class SimpleWorldLoader implements WorldLoader {

    private final Map<String, World> levels = new HashMap<>();

    public void addLevel(final String name, final World world) {
        this.levels.put(name, world);
    }

    public void removeLevel(final String name) {
        this.levels.remove(name);
    }

    @Override
    public World load(String name) {
        final World world = this.levels.get(name);
        if (world == null) {
            throw new LevelLoadException(String.format("world '%s' does not exist", name));
        } else {
            return world;
        }
    }
}
