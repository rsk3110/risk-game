package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Fortifies target territory by moving armies from origin.
 * Only works when origin borders target.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class FortifyCommand implements Command {

    public FortifyCommand() {
    }

    /**
     * Fortify cannot be executed without any arguments.
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        System.out.println("Invalid arguments. {fortify <origin> <target> <#armies>}");
        return false;
    }

    /**
     * Checks if player is targeting self-occupied territory with another self-occupied territory,
     * moves armies from origin to target if numArmies is at least 1 less than the territories army count.
     *
     * @param player player executing the command
     * @param args arguments of execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        if(args.size() != 3) {
            System.out.println("Invalid number of arguments. {fortify <origin> <target> <#armies>}");
            return false;
        }

        Territory origin = Territory.stringToTerritory(player, args.get(0));
        Territory target = Territory.stringToTerritory(player, args.get(1));
        int numArmies;
        try {
            numArmies = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of armies, not a number. {fortify <origin> <target> <#armies>}");
            return false;
        }

        if (origin == null || !origin.isOccupiedBy(player)) {
            System.out.println("origin not owned by player or does not exist. {fortify <origin> <target> <#armies>}");
            return false;
        } else if (target == null || !origin.isNeighbor(player.getWorld(), target)) {
            System.out.println("target is not bordering origin or does not exist. {fortify <origin> <target> <#armies>}");
            return false;
        } else if(!target.isOccupiedBy(player)) {
            System.out.println("target is not occupied by player. {fortify <origin> <target> <#armies>}\n" + target.toString());
            return false;
        } else if(numArmies > origin.getArmies() - 1) {
            System.out.println("Moving too many armies. You can move a max of '" + (origin.getArmies() - 1) + "' armies");
            return false;
        }

        origin.moveArmy(numArmies, target);
        return true;
    }
}