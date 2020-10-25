package io.github.rsk3110.riskgame;

import java.io.Serializable;
import java.util.Objects;
/**
 *
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

    public Territory(final int id) {
        this.id = id;
        this.armies = 0;
        this.occupant = null;
    }

    /**
     * Set territory name
     *
     * @param name name of territory
     * @return
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
     * @param armies integer value of the number of armies
     * @return
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
     * @param target territory to which armies are going
     * @return
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
     * Get territory's continent
     *
     * @return Continent that the territory belongs in
     */
    public Continent getContinent() {
        return this.continent;
    }

    /**
     * Set territory's occupant
     *
     * @param occupant Object of class Player, storing the player to be set as occupant
     * @return
     */
    public void setOccupant(final Player occupant) {
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
     * @return true if player occupies this territory
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
     * @return true if territory is neighbor
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
        return getName() + ": Occupied by '" + occupant.getName() + "' with '" + armies + "' armies.\n";
    }
}
