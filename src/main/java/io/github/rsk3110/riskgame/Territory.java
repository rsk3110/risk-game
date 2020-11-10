package io.github.rsk3110.riskgame;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Represents a Territory.
 * Tracks territory id, name, containing continent,
 * allocated armies, and occupying player.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/
public final class Territory implements Serializable {

    private final int id;
    private String name;
    private Continent continent;
    private int armies; // holds how many armies each team has
    private Player occupant;

    /**
     * Initializes a Territory object.
     *
     * @param id id of the territory
     */
    public Territory(final int id) {
        this.id = id;
        this.armies = 0;
        this.occupant = null;
    }

    /**
     * Get territory id
     *
     * @return id of the territory
     */
    private int getId() {
        return this.id;
    }

    /**
     * Set territory name
     *
     * @param name name of territory
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get territory name
     *
     * @return name of territory
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set number of armies
     *
     * @param armies number of armies
     */
    public void setArmies(int armies){
        this.armies = armies;
    }

    /**
     * Get number of armies
     *
     * @return number of armies
     */
    public int getArmies(){ return this.armies; }

    /**
     * Move number of armies from this territory to target territory
     *
     * @param num number of armies
     * @param target target territory
     */
    public void moveArmy(int num, Territory target) {
        this.armies -= num;
        target.setArmies(target.getArmies() + num);
    }

    /**
     * Reduces army on territory by 1
     */
    public void decrementArmies() {
        if(this.armies > 0) armies--;
    }

    /**
     * Set territory's continent
     *
     * @param continent stores the continent
     */
    public void setContinent(final Continent continent) {
        this.continent = continent;
    }

    /**
     * Get containing continent
     *
     * @return containing continent
     */
    public Continent getContinent() {
        return this.continent;
    }

    /**
     * Set occupant
     *
     * @param occupant player to occupy
     */
    public void setOccupant(Player occupant) {
        if(occupant != null) {
            removeOccupant();
            occupant.addTerritory(this);
        }

        this.occupant = occupant;
    }

    /**
     * Returns occupant of territory
     *
     * @return player that occupies territory
     */
    public Player getOccupant() {
        return this.occupant;
    }

    /**
     * Check if player occupies territory
     *
     * @param player player executing the command
     * @return whether player occupies territory
     */
    public boolean isOccupiedBy(Player player) {
        return this.occupant.equals(player);
    }

    /**
     * Remove occupant from territory
     */
    public void removeOccupant() {
        if(occupant != null) {
            occupant.removeTerritory(this);
            setOccupant(null);
        }
    }

    /**
     * Check if target territory is a neighbor of origin territory
     *
     * @param world game world
     * @param territory target territory
     * @return whether target territory is neighbor of origin territory
     */
    public boolean isNeighbor(World world, Territory territory) {
        return world.getGraph().containsEdge(this, territory);

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Territory that = (Territory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "[" + id + "] " + getName() + ": Occupied by '" + occupant.getName() + "' with '" + armies + "' armies.\n";
    }

    /**
     * Gets the territory associated with given id, or null
     * if no such territory exists.
     *
     * @param player player executing the command
     * @param id id of the territory
     * @return territory with given id
     */
    static public Territory idToTerritory(Player player, String id) {
        List<Object> territory = Arrays.asList(player.getWorld().getTerritoryMap().keySet().stream()
                .filter(t -> String.valueOf(t.getId()).equals(id)).toArray());
        return (territory.size() == 1) ? (Territory)territory.get(0) : null;
    }
}
