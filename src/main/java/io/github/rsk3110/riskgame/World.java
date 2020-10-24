package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.model.world.Continent;
import io.github.rsk3110.riskgame.model.world.TerritoryEdge;
import org.jgrapht.Graph;

import java.io.Serializable;
import java.util.List;

public final class World implements Serializable {
    private final Graph<Territory, TerritoryEdge> graph;
    private final List<Continent> continents;

    public World(final Graph<Territory, TerritoryEdge> graph, final List<Continent> continents) {
        this.graph = graph;
        this.continents = continents;
    }

    public Graph<Territory, TerritoryEdge> getGraph() {
        return this.graph;
    }

    public List<Continent> getContinents() {
        return this.continents;
    }
}
