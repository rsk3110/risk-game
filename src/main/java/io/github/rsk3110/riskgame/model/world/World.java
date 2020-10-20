package io.github.rsk3110.riskgame.model.world;

import org.jgrapht.Graph;

import java.util.List;

public final class World {
    private final Graph<TerritoryVertex, TerritoryEdge> graph;
    private final List<Continent> continents;

    public World(final Graph<TerritoryVertex, TerritoryEdge> graph, final List<Continent> continents) {
        this.graph = graph;
        this.continents = continents;
    }

    public Graph<TerritoryVertex, TerritoryEdge> getGraph() {
        return this.graph;
    }

    public List<Continent> getContinents() {
        return this.continents;
    }
}
