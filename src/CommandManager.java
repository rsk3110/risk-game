import java.util.*;

public class CommandManager {
    private Map<String, Command> commandMap;

    public CommandManager() {
        this.commandMap = new HashMap<String, Command>();
    }

    public void register(String aName, Command aCommand) {
        this.commandMap.put(aName, aCommand);
    }

    public void execute(String aName) {
        commandMap.get(aName).execute();
    }

    public void execute(String aName, List<String> args) {
        commandMap.get(aName).execute(args);
    }
}
