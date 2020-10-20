package io.github.rsk3110.riskgame.model.world.state;

import io.github.rsk3110.riskgame.model.world.Territory;
import io.github.rsk3110.riskgame.model.world.World;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class WorldState implements Serializable {
    private final World world;
    private final Map<Territory, TerritoryState> territoryOccupancy;

    public WorldState(final World world) {
        this.world = world;
        this.territoryOccupancy = new HashMap<>();
        for (final Territory t : world.getGraph().vertexSet()) {
            this.territoryOccupancy.put(t, new TerritoryState());
        }
    }

    public World getWorld() {
        return this.world;
    }
}
