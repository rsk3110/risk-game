package io.github.rsk3110.riskgame;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getBonusArmies() {
        return this.bonusArmies;
    }

    public Set<Territory> getTerritories() { return this.territories; }

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
