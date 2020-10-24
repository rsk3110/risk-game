import java.util.*;

public class Player {
    
    private String name;

    private List<Territory> territories;

    public Player(String aName) {
        this.name = aName;
        this.territories = new ArrayList<Territory>();
    }

    public String getName() {
        return name;
    }

    public void addTerritory(Territory aTerritory) {
        this.territories.add(aTerritory);
    }

    public void removeTerritory(Territory aTerritory) {
        this.territories.remove(aTerritory);
    }

    public List<Territory> getTerritories() {
        return territories;
    }
}

