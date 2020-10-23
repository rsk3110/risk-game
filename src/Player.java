import java.util.*;

public class Player {

    private String name;
    private List<Territory> currPlayersTerritories;

    public Player(String aName, List<Territory> someTerritories) {
        this.name = aName;
        this.currPlayersTerritories = someTerritories;
    }

    public List<Territory> getTerritories() {
        return currPlayersTerritories;
    }

    public void addTerritories(Territory territory) {
        currPlayersTerritories.add(territory);
    }
}

