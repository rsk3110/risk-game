package io.github.rsk3110.riskgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a player.
 * Tracks player name, occupied territories, world object,
 * and unallocated armies.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/

public class Player {

    private String name;
    private List<Territory> territories;
    private World world;
    private int unallocatedArmies;

    /**
     * Initializes a Player object.
     *
     * @param world world file for the game
     * @param aName name of the player
     * @param armies armies to allocate to player
     */
    public Player(World world, String aName, int armies) {
        this.name = aName;
        this.territories = new ArrayList<Territory>();
        this.world = world;
        this.unallocatedArmies = armies;
    }

    /**
     * Get name of player
     *
     * @return the name of the player
     */
    public String getName() { return this.name; }

    /**
     * Add territory to players occupied territory list
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
     * Get occupied territories
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
     * @return whether the player occupies the territory
     */
    public boolean isOccupying(Territory territory) {
        return territories.contains(territory);
    }

    /**
     * Get number of unallocated armies
     *
     * @return number of unallocated armies
     */
    public int getArmies() {
        return this.unallocatedArmies;
    }

    /**
     * Set number of unallocated armies
     *
     * @param num number of armies to set to
     */
    public void setArmies(int num) {
        this.unallocatedArmies = num;
    }

    /**
     * Move armies from player to territory
     *
     * @param num number of armies to move
     * @param territory territory to move armies to
     */
    public void allocateArmies(int num, Territory territory) {
        setArmies(getArmies() - num);
        territory.setArmies(territory.getArmies() + num);
    }

    /**
     * Add armies to player depending on number of territories and
     * continents controlled.
     */
    public void updateArmies() {
        this.unallocatedArmies += (territories.size() >= 9) ? territories.size() / 3 : 3; // Always at least 3.
        for(Continent continent : getOccupiedContinents()) {
            this.unallocatedArmies += continent.getBonusArmies();
        }
    }


    /**
     * Get players world
     *
     * @return the player's world
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Get set of Continents the player is
     * fully occupying.
     *
     * @return set of Continents fully occupied by player
     */
    public Set<Continent> getOccupiedContinents() {
        Set<Continent> continentSet = new HashSet<Continent>();
        for(Continent continent : world.getContinents()) {
            boolean add = true;
            for(Territory territory : continent.getTerritories()) {
                if (!territory.getOccupant().equals(this)) {
                    add = false;
                    break;
                }
            }

            if(add) continentSet.add(continent);
        }

        return continentSet;
    }
}

