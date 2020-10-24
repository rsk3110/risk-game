import java.util.*;

public interface Command {
    public boolean execute(Player player);
    public boolean execute(Player player, List<String> args);
}
