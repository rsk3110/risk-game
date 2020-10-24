package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.model.world.state.WorldState;
import io.github.rsk3110.riskgame.World;
import io.github.rsk3110.riskgame.controller.world.loader.WorldLoader;

public class RiskController {
    private final WorldLoader worldLoader;
    private WorldState worldState;

    public RiskController(final WorldLoader worldLoader) {
        this.worldLoader = worldLoader;
    }

    public void createNewGame(final String worldName) {
        this.worldState = new WorldState(this.worldLoader.load(worldName));
    }

    public WorldState getWorldState() {
        return this.worldState;
    }

    public World getWorld() {
        return this.getWorldState().getWorld();
    }
}
