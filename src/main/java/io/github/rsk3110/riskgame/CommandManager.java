package io.github.rsk3110.riskgame;

import java.util.*;

public class CommandManager {
    private Map<String, Command> commandMap;

    public CommandManager() {
        this.commandMap = new HashMap<String, Command>();
    }

    public void register(String aName, Command aCommand) {
        this.commandMap.put(aName, aCommand);
    }

    public boolean execute(Player player, String aName) {
        return commandMap.get(aName).execute(player);
    }

    public boolean execute(Player player, String aName, List<String> args) {
        return commandMap.get(aName).execute(player, args);
    }

    public boolean handleInput(Player player, String input) {
        List<String> inputList = new ArrayList<String>(Arrays.asList(input.toLowerCase().split(" ")));
        String cmd = inputList.get(0);

        if(!commandMap.containsKey(cmd)) System.out.println("Invalid command!");
        else if(inputList.size() == 1) return this.execute(player, cmd);
        else return this.execute(player, cmd, inputList.subList(1, inputList.size() - 1));

        return false;
    }
}
