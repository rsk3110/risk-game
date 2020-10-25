package io.github.rsk3110.riskgame;

import java.util.List;
import java.util.ArrayList;

public class Player {

    private String name;
    private List<Territory> territories;

    public Player(String aName) {
        this.name = aName;
        this.territories = new ArrayList<Territory>();
    }

    public String getName() { return this.name; }

    public void addTerritory(Territory aTerritory) {
        this.territories.add(aTerritory);
    }

    public void removeTerritory(Territory aTerritory) {
        this.territories.remove(aTerritory);
    }

    public List<Territory> getTerritories() {
        return territories;
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

