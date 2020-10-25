package io.github.rsk3110.riskgame;

import org.jgrapht.Graph;

import java.io.Serializable;
import java.util.*;

public final class World implements Serializable {
    private final Graph<Territory, TerritoryEdge> graph;
    private final List<Continent> continents;
    private final Map<Territory, Set<TerritoryEdge>> territoryMap;

    public World(final Graph<Territory, TerritoryEdge> graph, final List<Continent> continents) {
        this.graph = graph;
        this.territoryMap = new HashMap<Territory, Set<TerritoryEdge>>() {{
            for(Territory territory : graph.vertexSet()) {
                put(territory, graph.edgesOf(territory));
            }
        }};
        this.continents = new ArrayList<Continent>(continents);
        for(Continent continent : continents) {
            for(Territory territory : territoryMap.keySet())
                if(territory.getContinent().equals(continent)) continent.addTerritory(territory);
        }
    }

    public Graph<Territory, TerritoryEdge> getGraph() {
        return graph;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public Map<Territory, Set<TerritoryEdge>> getTerritoryMap() {
        return territoryMap;
    }
}
