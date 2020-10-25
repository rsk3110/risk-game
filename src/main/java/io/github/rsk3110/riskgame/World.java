package io.github.rsk3110.riskgame;

import org.jgrapht.Graph;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a game World.
 * Stores the graph (Territories & Relationships),
 * continents, and a map of territories to territory edge sets.
 *
 * @author Mark Johnson
 * @author Kaue Gomes e Sousa de Oliveira
 */
public final class World implements Serializable {
    private final Graph<Territory, TerritoryEdge> graph;
    private final List<Continent> continents;
    private final Map<Territory, Set<TerritoryEdge>> territoryMap; // map of territories to their sets of territory edges

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
                if(territory.getContinent().equals(continent)) continent.addTerritory(territory); // add territory to containing continent
        }
    }

    /**
     * Get Graph
     *
     * @return graph of world
     */
    public Graph<Territory, TerritoryEdge> getGraph() {
        return graph;
    }

    /**
     * Get Continents
     *
     * @return continents of world
     */
    public List<Continent> getContinents() {
        return continents;
    }

    /**
     * Get Map of Territory to Set of TerritoryEdges
     *
     * @return map of territories to their territory edges
     */
    public Map<Territory, Set<TerritoryEdge>> getTerritoryMap() {
        return territoryMap;
    }
}
