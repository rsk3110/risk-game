package io.github.rsk3110.riskgame;

import javax.swing.*;

/**
 * Fortifies target territory by moving armies from origin.
 * Only works when both:
 * - origin and target are occupied by player
 * - origin borders target.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class FortifyCommand implements Command {

    public FortifyCommand() {}

    /**
     * Executes the attack command.
     * If valid territories are entered, move count armies from origin to target.
     * @param player player executing the command
     * @param origin Territory to fortify with
     * @param target Territory to fortify
     * @param count number of armies to fortify with
     * @return whether the command was successful
     */
    public static boolean execute(Player player, Territory origin, Territory target, int count) {
        if (origin == null || !origin.isOccupiedBy(player)) {
            JOptionPane.showMessageDialog(null, "origin not owned by player or does not exist.");
            return false;
        } else if (target == null || !origin.isNeighbor(player.getWorld(), target)) {
            JOptionPane.showMessageDialog(null, "target is not bordering origin or does not exist.");
            return false;
        } else if(!target.isOccupiedBy(player)) {
            JOptionPane.showMessageDialog(null, "target is not occupied by player.\n" + target.toString());
            return false;
        } else if(count > origin.getArmies() - 1) {
            JOptionPane.showMessageDialog(null, "Moving too many armies. You can move a max of '" + (origin.getArmies() - 1) + "' armies");
            return false;
        }

        origin.moveArmy(count, target);
        return true;
    }
}