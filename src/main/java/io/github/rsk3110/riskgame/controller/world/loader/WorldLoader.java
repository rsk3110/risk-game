package io.github.rsk3110.riskgame.controller.world.loader;

import io.github.rsk3110.riskgame.World;

public interface WorldLoader {
    World load(String name);
}
