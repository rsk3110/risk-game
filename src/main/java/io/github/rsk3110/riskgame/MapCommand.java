package io.github.rsk3110.riskgame;

import java.util.List;

/**
 * Lists a map of the World in format:
 * ContinentName (Bonus Armies: #) {
 *     TerritoryName: Occupied by PlayerName with # armies.
 *     ...
 * }
 * ...
 *
 * @author Kaue Gomes e Sousa de Oliveira
 */
public class MapCommand implements Command {

    public MapCommand(){
    }

    /**
     * Prints all the continents and their territories to console.
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        for(Continent continent : player.getWorld().getContinents()) {
            System.out.println(continent);
        }
        return false;
    }

    /**
     * Map cannot be executed with any arguments.
     *
     * @param player player executing the command
     * @param args arguments of execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        System.out.println("Invalid arguments. Quit does not take in any arguments. {map}");
        return false;
    }
}
