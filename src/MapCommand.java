import java.util.List;

public class MapCommand implements Command {

    public MapCommand(){
    }

    public boolean execute(Player player) {
        return false;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
