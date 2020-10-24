import java.util.ArrayList;
import java.util.List;

public class SkipCommand implements Command {

    Player player;

    public SkipCommand(){
    }

    public boolean execute(Player player) {
        return true;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
