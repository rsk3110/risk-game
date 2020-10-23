import java.util.HashMap;

public class Territory {

    private String name;
    private Continent continent;
    private Player owner;

    private int armies;

    //stores different borders to the currTerritory
    private HashMap<String, Territory> borderTerritories;

    public Territory(String territoryName, Player owner, Continent aContinent) {
        this.name = territoryName;
        this.continent = aContinent;
        this.owner = owner;

        this.armies = 0;

        borderTerritories = new HashMap<>();
    }

    public void setArmies(int armies){
        this.armies = armies;
    }

    public int getArmies(){
        return this.armies;
    }

    public Territory getBorderTerritory(String direction)
    {
        return borderTerritories.get(direction);
    }
}