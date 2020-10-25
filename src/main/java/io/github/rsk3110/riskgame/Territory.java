package io.github.rsk3110.riskgame;

import java.io.Serializable;
import java.util.Objects;

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

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    //get the number of armies
    public int getArmies(){ return this.armies; }

    //set the number of armies
    public void setArmies(int armies){
        this.armies = armies;
    }

    public void moveArmy(int num, Territory target) {
        this.armies -= num;
        target.setArmies(target.getArmies() + num);
    }

    public void decrementArmies() {
        if(this.armies > 0) armies--;
    }

    public void setContinent(final Continent continent) {
        this.continent = continent;
    }

    public Continent getContinent() {
        return this.continent;
    }

    public void setOccupant(final Player occupant) {
        if(occupant != null) {
            removeOccupant();
            occupant.addTerritory(this);
        }

        this.occupant = occupant;
    }

    public Player getOccupant() {
        return this.occupant;
    }

    public boolean isOccupiedBy(Player player) {
        return this.occupant.equals(player);
    }

    public void removeOccupant() {
        if(occupant != null) {
            occupant.removeTerritory(this);
            setOccupant(null);
        }
    }

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
