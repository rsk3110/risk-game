import java.util.List;

public class MapCommand implements Command {

    public MapCommand(){
    }

    //Code map in here, so that map can be display the during attack command
    public static void map(){

    }
    public boolean execute(Player player) {
        map();
        return false;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
