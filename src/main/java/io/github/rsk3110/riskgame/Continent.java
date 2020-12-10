package io.github.rsk3110.riskgame;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Continent.
 * Tracks name, color, bonus armies,
 * and contained territories.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/
public final class Continent implements Serializable {
    private final String name;
    private final Color color;
    private final int bonusArmies; // armies awarded per turn for continent control
    private final Set<Territory> territories;

    /**
     * Initializes a Continent object.
     *
     * @param name name of continent
     * @param color color of continent
     * @param bonusArmies bonus armies of continent
     */
    public Continent(final String name, final Color color, final int bonusArmies) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonusArmies;
        this.territories = new HashSet<Territory>();
    }

    /**
     * Gets name of continent
     *
     * @return name of the continent
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets color of continent
     *
     * @return color of the continent
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets number of bonus armies
     *
     * @return number of bonus armies for the continent
     */
    public int getBonusArmies() {
        return this.bonusArmies;
    }

    /**
     * Gets territories in continent
     *
     * @return set of territories in the continent
     */
    public Set<Territory> getTerritories() { return this.territories; }

    /**
     * Adds territories to continent
     *
     * @param territory territory to add
     */
    public void addTerritory(Territory territory) { if(territory != null) territories.add(territory); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Continent continent = (Continent) o;
        return Objects.equals(name, continent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Override toString to benefit MapCommand.
     *
     * @return String representation of continent
     */
    @Override
    public String toString() {
        return getName();
    }
}
