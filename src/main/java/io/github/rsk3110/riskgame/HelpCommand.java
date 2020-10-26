package io.github.rsk3110.riskgame;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Displays information on commands and how to execute them.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 **/
public class HelpCommand implements Command {

    private Map<String, String> shortMessages;
    private Map<String, String> longMessages;

    /**
     * Initializes short and long(more detailed) messages in Tree with command name and description
     */
    public HelpCommand(){
        this.shortMessages = new TreeMap<String, String>(){{
            put("help", "help | help <command> - tells you how to play the game. Optionally explains command if specified.");
            put("map", "map - Shows all continents' and territories' statuses.");
            put("attack", "attack <origin> <target> - Attacks target territory from origin territory. See 'help attack' for more.");
            put("fortify", "fortify <origin> <target> <#armies> - Moves armies from origin to target if both are self-occupied and #armies is 1 less than origin army count.");
            put("skip", "skip - skips your turn and goes to the next player.");
            put("quit", "quit - quits the program.");
        }};

        this.longMessages = new TreeMap<String, String>(){{
            put("help", shortMessages.get("help"));
            put("map", shortMessages.get("map"));
            put("attack", "attack <origin> <target> - Attacks target territory from origin territory.\n" +
                    "Attacker chooses 1 - 3 dice, requiring the number of dice + 1 armies at origin to select.\n" +
                    "Defender choose 1 - 2 dice, requiring the number of dice + 1 armies at target to select.\n" +
                    "Highest die are compared until territory is lost (target army hits 0), or a player uses up all their dice.\n" +
                    "If attack wins comparison, target loses 1 army. If defender wins or ties, origin loses 1 army.");
            put("fortify", shortMessages.get("fortify"));
            put("skip", shortMessages.get("skip"));
            put("quit", shortMessages.get("quit"));
        }};
    }

    /**
     * Print all shortMessages
     *
     * @param player player executing the command
     * @return whether to hand control to next player
     */
    public boolean execute(Player player) {
        for(String cmd : shortMessages.keySet())
            System.out.println(shortMessages.get(cmd));

        return false;
    }

    /**
     * Prints longMessage of specified command, if valid.
     *
     * @param player player executing the command
     * @param args arguments of execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, List<String> args) {
        if(args.size() != 1) {
            System.out.println("Invalid number of arguments. {help | help <command>}");
            return false;
        }

        String cmd = args.get(0);
        if(!longMessages.containsKey(cmd)) {
            System.out.println("Command '" + cmd + "'  does not exist. {help | help <command>}");
            return false;
        }

        System.out.println(longMessages.get(cmd));
        return false;
    }
}
