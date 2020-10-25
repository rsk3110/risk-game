package io.github.rsk3110.riskgame;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Creates continents and sets each territory in them
 *
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/
public final class Continent implements Serializable {
    private final String name;
    private final Color color;
    private final int bonusArmies;
    private final Set<Territory> territories;

    public Continent(final String name, final Color color, final int bonusArmies) {
        this.name = name;
        this.color = color;
        this.bonusArmies = bonusArmies;
        this.territories = new HashSet<Territory>();
    }

    /**
     * Get name of continent
     *
     * @return the name of the continent
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the color of this continent
     *
     * @return the color of continent
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets the number of bonus armies
     *
     * @return the number of bonus armies allocated to this continent
     */
    public int getBonusArmies() {
        return this.bonusArmies;
    }

    /**
     * Gets the territories in this continent
     *
     * @return the set of territories in this continent
     */
    public Set<Territory> getTerritories() { return this.territories; }

    /**
     * Adds territories to the continent
     *
     * @param territory territory that belongs in this continent
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + " (Bonus Armies:" + bonusArmies + ") {\n");
        for(Territory territory : territories) {
            sb.append("\t" + territory.toString());
        }

        return sb.toString();
    }
}
