import java.util.*;

public class Player {

    private String name;
    private List<Territory> territories;

    public Player(String aName, List<Territory> someTerritories) {
        this.name = aName;
        this.territories = someTerritories;
    }
}

