package io.github.rsk3110.riskgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    private String name;
    private List<Territory> territories;
    private World world;
    private int unallocatedArmies;

    public Player(World world, String aName, int maxArmies) {
        this.name = aName;
        this.territories = new ArrayList<Territory>();
        this.world = world;
        this.unallocatedArmies = maxArmies;
    }

    public String getName() { return this.name; }

    public void addTerritory(Territory aTerritory) {
        this.territories.add(aTerritory);
    }

    public void removeTerritory(Territory aTerritory) {
        this.territories.remove(aTerritory);
    }

    public List<Territory> getTerritories() {
        return this.territories;
    }

    public boolean isOccupying(Territory territory) {
        return territories.contains(territory);
    }

    public int getArmies() {
        return this.unallocatedArmies;
    }

    public void setArmies(int num) {
        this.unallocatedArmies = num;
    }

    public void allocateArmies(int num, Territory territory) {
        setArmies(getArmies() - num);
        territory.setArmies(territory.getArmies() + num);
    }

    public World getWorld() { return this.world; };

    public void updateArmies() {
        this.unallocatedArmies += (territories.size() >= 9) ? territories.size() / 3 : 3; // Always at least 3.
        for(Continent continent : getOccupiedContinents()) {
            this.unallocatedArmies += continent.getBonusArmies();
        }
    }

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.name + ":\n");
        for(Territory territory : territories) {
            stringBuilder.append(territory.toString() + '\n');
        }

        return stringBuilder.toString();
    };
}

