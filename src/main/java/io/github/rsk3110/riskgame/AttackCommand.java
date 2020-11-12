package io.github.rsk3110.riskgame;

import javax.swing.*;
import java.util.*;

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

    public AttackCommand(){
    }

    /**
     * Executes the attack command.
     * Checks for valid number of arguments entered {attack origin target}
     * Checks if valid territories were selected, if not end turn
     * If valid territories are entered, move on to (call method) attack.
     *
     * @param player player executing the command
     * @param args stores arguments listed after the command attack
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        if(args.size() != 2) {
            System.out.println("Invalid number of arguments.");
            return false;
        }
        Territory origin = Territory.nameToTerritory(player, args.get(0));
        Territory target = Territory.nameToTerritory(player, args.get(1));

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
        else
            return attack(player, origin, target);
    }

    /**
     * Asks the player for the number of dice to roll.
     * Checks if player entered a valid number by checking against the max
     * allowed number of dice.
     *
     * @param player player executing the command
     * @param max maximum number of dice that can be rolled
     * @return the number of dice that player chose to roll
     */
    private int getNumDice(Player player, int max) {
        try {
            Scanner scanner = new Scanner(System.in);
            JOptionPane.showMessageDialog(null, "Roll how many dice? (" + player.getName() + ") Must be " + ((max == 1) ? "1" : "1 to " + max + "."));
            int num;
            do {
                num = scanner.nextInt();
                if(!(num > 0 && num <= max)) System.out.println("Invalid number. Must be " + ((max == 1) ? "1" : "1 to " + max + "."));
            } while(!(num > 0 && num <= max));
            return num;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Input. Try a new number of die.");
            return getNumDice(player, max);
        }
    }

    /**
     * Generates values between 1 and 6 and adds them to a list.
     *
     * @param player player executing the command
     * @param max valid maximum number of dice that can be rolled
     * @return the list of dice values
     */
    private List<Integer> getDiceValues(Player player, int max) {
        return new ArrayList<Integer>(){{
            Random random = new Random();
            for(int i = getNumDice(player, max); i > 0; i--) {
                int num = random.nextInt(6) + 1;
                add(num);
                JOptionPane.showMessageDialog(null, "You rolled: " + num);
            }
        }};
    }

    /**
     * Prompts the player for the number of armies to move from
     * one territory to another.
     *
     * @param min minimum number of armies that can be moved
     * @param max maximum number of armies that can be moved
     * @return number of armies to move
     */
    private int getNumArmyToMove(int min, int max) {
        try {
            Scanner scanner = new Scanner(System.in);
            JOptionPane.showMessageDialog(null, "Move how many armies? (" + min + " to " + max + ")");
            int num;
            do {
                System.out.print("> ");
                num = scanner.nextInt();
                if(!(num >= min && num <= max)) System.out.println("Invalid number. Must be " + ((max == min) ? min : min + " to " + max + "."));
            } while(!(num >= min && num <= max));
            return num;
        } catch (Exception e) {
            System.out.println("Invalid Input. Try a new number of armies.");
            return getNumArmyToMove(min, max);
        }
    }

    /**
     * Rolls the dice for both origin and target territories, and compares the dice values.
     * If dice are equal or target > origin, origin territory loses battle and 1 army
     * If origin dice > target dice, target territory loses battle and 1 army.
     * If target territory armies hit 0, player occupies target and is prompted
     * for number of armies to move.
     *
     * @param player player executing the command
     * @param origin Territory formulating attack
     * @param target Territory defending from attack
     * @return whether to hand control to next player
     */
    private boolean attack(Player player, Territory origin, Territory target){
        List<Integer> playerValues = getDiceValues(player, Math.min(3, origin.getArmies() - 1));
        List<Integer> targetValues = getDiceValues(target.getOccupant(), Math.min(2, target.getArmies()));
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
                    origin.moveArmy(getNumArmyToMove(attackerDiceCount - lostCount, origin.getArmies() - 1), target);
                    JOptionPane.showMessageDialog(null, player.getName() + " captured " + target.getName() + " and it now holds " + target.getArmies() + " armies.");
                    if(tOccupant.getTerritories().size() == 0) System.out.println(tOccupant.getName() + " was eliminated.");
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * Attack is a multiple argument command.
     * Cannot be executed with no commands.
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        System.out.println("Invalid arguments. {attack <origin> <target>}");
        return false;
    }
}
