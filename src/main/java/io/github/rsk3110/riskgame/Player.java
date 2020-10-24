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

    public void addTerritory(Territory aTerritory) {
        this.territories.add(aTerritory);
    }

    public void removeTerritory(Territory aTerritory) {
        this.territories.remove(aTerritory);
    }
}

