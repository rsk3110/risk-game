package io.github.rsk3110.riskgame.controller;

import io.github.rsk3110.riskgame.model.world.state.WorldState;
import io.github.rsk3110.riskgame.model.world.World;
import io.github.rsk3110.riskgame.controller.world.loader.WorldLoader;
import io.github.rsk3110.riskgame.view.RiskView;

import java.util.function.Function;

public class RiskController {
    private final WorldLoader worldLoader;
    private final RiskView gameView;
    private WorldState worldState;

    public RiskController(final Function<RiskController, RiskView> viewFunction, final WorldLoader worldLoader) {
        this.worldLoader = worldLoader;
        this.gameView = viewFunction.apply(this);
    }

    public void run() {
        for (;;) {

        }
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
