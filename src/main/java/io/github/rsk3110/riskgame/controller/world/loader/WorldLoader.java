package io.github.rsk3110.riskgame.controller.world.loader;

import io.github.rsk3110.riskgame.model.world.World;

public interface WorldLoader {
    World load(String name);
}
