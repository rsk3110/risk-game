package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.controller.GameController;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a player.
 * Tracks player name, occupied territories, world object,
 * and unallocated armies.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/

public class AI extends Player implements Serializable {

    /**
     * Initializes an AI object.
     *
     * @param world world file for the game
     * @param aName name of the player
     * @param armies armies to allocate to player
     */
    public AI(World world, String aName, int armies) {
        super(world, aName, armies);
    }

    /**
     * Simulates AI gameplay by randomly selecting a territory.
     * @param gameController controller for game instance
     */
    public void play(GameController gameController) {
        int max = 0;
        Territory territoryAI = null;
        for(Territory t : this.getTerritories())
        {
            if(t.getArmies() > max) {
                max = t.getArmies();
                territoryAI = t; //highest army territory owned by AI player
            }
        }

        ArrayList<Territory> neighborT = new ArrayList<>();

        //finds neighbor
        for(Territory t : territories){
            if(territoryAI.isNeighbor(world, t)) {
                neighborT.add(t);
            }
        }

        Territory temp = territoryAI; //Just a to avoid setting it to null, if no neighboring territory has lower armies than AI's territory
        for(Territory t : neighborT)
        {
            if(t.getArmies() < territoryAI.getArmies()) {
                temp = t; //lowest army territory neighboring the AI players territory
            }
        }

        if(temp == territoryAI){
            gameController.skipTurn();
            JOptionPane.showMessageDialog(null, getName() + " skipped.");
        }
        if(temp.isOccupiedBy(this)){
            int armies = territoryAI.getArmies() - 1;
            gameController.fortify(territoryAI, temp, armies);
            JOptionPane.showMessageDialog(null, getName() + " fortified " + temp.getName() + "\nwith " + armies + " armies from " + territoryAI.getName());
        }
        else if(!temp.isOccupiedBy(this)){
            int attacking = (int)(Math.random() * (territoryAI.getArmies() - 1) - 1);
            gameController.attack(territoryAI, temp, 1, Helper.promptForIntegerValue(1, temp.getArmies(), temp.getOccupant().getName() + ": How many dice would you like to roll?"));
        }
        else{
            gameController.skipTurn();
        }
    }

}

