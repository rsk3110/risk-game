package io.github.rsk3110.riskgame.model.world.state;

import io.github.rsk3110.riskgame.model.Player;
import io.github.rsk3110.riskgame.model.world.TerritoryVertex;
import io.github.rsk3110.riskgame.model.world.World;

import java.util.HashMap;
import java.util.Map;

public final class WorldState {
    private final World world;
    private final Map<TerritoryVertex, TerritoryState> territoryOccupancy;

    public WorldState(final World world) {
        this.world = world;
        this.territoryOccupancy = new HashMap<>();
        for (final TerritoryVertex t : world.getGraph().vertexSet()) {
            this.territoryOccupancy.put(t, new TerritoryState());
        }
    }

    public World getWorld() {
        return this.world;
    }
}
