package io.github.rsk3110.riskgame;

import java.util.*;

/**
 * Stores and handles command execution.
 *
 * @author Kaue Gomes e Sousa de Oliveira
 **/
public class CommandManager {

    private Map<String, Command> commandMap; // Mapping of command name to command object

    public CommandManager() {
        this.commandMap = new HashMap<String, Command>();
    }

    /**
     * Registers a Command object to commandMap, indexed by its name.
     *
     * @param aName name of command to store
     * @param aCommand command object to store
     */
    public void register(String aName, Command aCommand) {
        this.commandMap.put(aName, aCommand);
    }

    /**
     * Call execute method of provided command.
     *
     * @param player player executing the command
     * @param aName name of command to execute
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, String aName) {
        return commandMap.get(aName).execute(player);
    }

    /**
     * Calls execute method of provided command with provided arguments.
     *
     * @param player player executing the command
     * @param aName name of command to execute
     * @param args arguments to pass into command execution
     * @return whether to hand control to next player
     */
    public boolean execute(Player player, String aName, List<String> args) {
        return commandMap.get(aName).execute(player, args);
    }

    /**
     * Splits input and calls relevant Command object execute method
     * if command name is found in commandMap
     *
     * @param player the player executing the command
     * @param input the input to split and execute
     * @return whether to hand control to next player
     */
    public boolean handleInput(Player player, String input) {
        List<String> inputList = new ArrayList<String>(Arrays.asList(input.toLowerCase().split(" ")));
        String cmd = inputList.get(0);

        if(!commandMap.containsKey(cmd)) System.out.println("Invalid command!");
        else if(inputList.size() == 1) return this.execute(player, cmd);
        else return this.execute(player, cmd, inputList.subList(1, inputList.size()));

        return false;
    }
}
