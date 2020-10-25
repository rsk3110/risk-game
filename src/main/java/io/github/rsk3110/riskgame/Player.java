package io.github.rsk3110.riskgame;

import java.util.List;
import java.util.ArrayList;

/**
 *
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/

public class Player {

    private String name;
    private List<Territory> territories;
    private World world;
    private int maxArmies;

    public Player(World world, String aName, int maxArmies) {
        this.name = aName;
        this.territories = new ArrayList<Territory>();
        this.world = world;
        this.maxArmies = maxArmies;
    }

    /**
     * Get name of player
     *
     * @return the name of the player
     */
    public String getName() { return this.name; }

    /**
     * Add territory to players occupying territory list
     *
     * @param aTerritory the territory to be added
     */
    public void addTerritory(Territory aTerritory) {
        this.territories.add(aTerritory);
    }

    /**
     * Get name of continent
     *
     * @return the name of the continent
     */
    public void removeTerritory(Territory aTerritory) {
        this.territories.remove(aTerritory);
    }

    /**
     * Get the players occupying territories
     *
     * @return list of occupied territories
     */
    public List<Territory> getTerritories() {
        return this.territories;
    }

    /**
     * Checks if player is occupying territory
     *
     * @param territory territory to check
     * @return true if player is occupying the territory
     */
    public boolean isOccupying(Territory territory) {
        return territories.contains(territory);
    }

    /**
     * Get players world
     *
     * @return the players world
     */
    public World getWorld() { return this.world; };

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.name + ":\n");
        for(Territory territory : territories) {
            stringBuilder.append(territory.toString() + '\n');
        }

        return stringBuilder.toString();
    };
}

