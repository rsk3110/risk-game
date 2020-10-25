package io.github.rsk3110.riskgame;

import java.util.List;
import java.util.ArrayList;

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

    public void decrementArmies() {
        this.unallocatedArmies--;
    }

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

