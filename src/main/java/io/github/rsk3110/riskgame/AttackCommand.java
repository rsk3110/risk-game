package io.github.rsk3110.riskgame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Rules for Attack command.
 * If player attacks a opposing player territory, the user will decide how many dice they want to roll.
 * Territory must have (#dice + 1) armies to roll that amount of dice;
 * The territory that loses a confrontation will lose 1 army (#confrontations = minimum #dice of attacker and defender, defender wins on tie)
 * If defender's army drops to, attacker claims target territory and moves (#dice) armies from origin to target.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 **/
public class AttackCommand implements Command {

    public AttackCommand() {}

    /**
     * Executes the attack command.
     * If valid territories are entered, move on to (call method) attack.
     *
     * @param player player executing the command
     * @param origin Territory attacking territory
     * @param target Territory attacked territory
     * @param attackingArmies int number of attacking armies
     * @param defendingArmies int number of defending armies
     * @return whether the command was successful
     */
    public static boolean execute(Player player, Territory origin, Territory target, int attackingArmies, int defendingArmies) {
        if (origin == null || !origin.isOccupiedBy(player)) {
            JOptionPane.showMessageDialog(null, "origin not occupied by player or does not exist.");
            return false;
        } else if (target == null || !origin.isNeighbor(player.getWorld(), target)) {
            JOptionPane.showMessageDialog(null, "target is not bordering origin or does not exist.");
            return false;
        } else if (origin.getArmies() <= 1 ) {
            JOptionPane.showMessageDialog(null, "origin has insufficient armies. Must be greater than 1.");
            return false;
        }

        if(target.isOccupiedBy(player)) {
            JOptionPane.showMessageDialog(null, "Can't attack own territory. Try fortify?");
            return false;
        }

        attack(player, origin, target, attackingArmies, defendingArmies);
        return true;
    }

    /**
     * Rolls the dice for both origin and target territories, and compares the dice values.
     * If dice are equal or target > origin, origin territory loses battle and 1 army
     * If origin dice > target dice, target territory loses battle and 1 army.
     * If target territory armies hit 0, player occupies target and is prompted
     * for number of armies to move.
     *
     * @param player player executing the command
     * @param origin formulating attack
     * @param target defending from attack
     */
    private static void attack(Player player, Territory origin, Territory target, int attackingArmies, int defendingArmies){
        List<Integer> playerValues = getDiceValues(Math.min(3, origin.getArmies() - 1), attackingArmies);
        List<Integer> targetValues = getDiceValues(Math.min(2, target.getArmies()), defendingArmies);
        JOptionPane.showMessageDialog(null,
                player.getName() + " rolled: " + playerValues + "\n"
                        + target.getOccupant().getName() + " rolled: " + targetValues);
        int minDice = Math.min(playerValues.size(), targetValues.size()); // set minimum amount of dice to roll
        int attackerDiceCount = playerValues.size();
        int lostCount = 0;
        for(int i = minDice; i > 0; i--) {
            Integer playerValue = Collections.max(playerValues);
            playerValues.remove(playerValue);
            Integer targetValue = Collections.max(targetValues);
            targetValues.remove(targetValue);

            if(playerValue < targetValue || playerValue.equals(targetValue)) {
                origin.decrementArmies();
                lostCount++;
                JOptionPane.showMessageDialog(null, "Failure! You lost an army. " + "{" + playerValue + " vs " + targetValue + "}");
            } else {
                target.decrementArmies();
                JOptionPane.showMessageDialog(null, "Success! Enemy lost an army. " + "{" + playerValue + " vs " + targetValue + "}");
                if(target.getArmies() == 0) { //if target defeated
                    Player tOccupant = target.getOccupant();
                    target.setOccupant(player);
                    JOptionPane.showMessageDialog(null, "Success! You won the battle.");
                    int userInput = Helper.promptForIntegerValue(attackerDiceCount - lostCount,origin.getArmies() - 1, "Move how many armies?");
                    origin.moveArmy(userInput, target);
                    JOptionPane.showMessageDialog(null, player.getName() + " captured " + target.getName() + " and it now holds " + target.getArmies() + " armies.");
                    if(tOccupant.getTerritories().size() == 0) System.out.println(tOccupant.getName() + " was eliminated.");
                    return;
                }
            }
        }
    }

    /**
     * Generates values between 1 and 6 and adds them to a list.
     *
     * @param max maximum number of dice that can be rolled
     * @param numRolls number of rolls
     * @return the list of dice values
     */
    private static List<Integer> getDiceValues(int max, int numRolls) {
        return new ArrayList<Integer>(){{
            Random random = new Random();
            for(int i = numRolls; i > 0; i--) {
                int num = random.nextInt(6) + 1;
                add(num);
            }
        }};
    }
}
