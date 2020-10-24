package io.github.rsk3110.riskgame;

import java.util.ArrayList;
import java.util.HashMap;

public class Territory {

    private String name;
    private Continent continent;

    private int armies; // holds how many armies each team has

    /*
    * stores surrounding territory borders to the currTerritory
    *  - String is the direction at which the territory is
    *  - Territory is the territory that is in the direction specified by string
    * */
    private ArrayList<Territory> borderTerritories;

    public Territory(String territoryName, Continent aContinent) {
        this.name = territoryName;
        this.continent = aContinent;

        this.armies = 0;

        borderTerritories = new ArrayList<>();
    }

    //get name of territory
    public String getName() {
        return name;
    }

    //set the number of armies
    public void setArmies(int armies){
        this.armies = armies;
    }

    //get the number of armies
    public int getArmies(){
        return this.armies;
    }

    //Set border territories
    public void setBorderTerritory(Territory territory)
    {
        borderTerritories.add(territory);
    }

    //get border territories
    public ArrayList<Territory> getBorderTerritories()
    {
        return borderTerritories;
    }
}