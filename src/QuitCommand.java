import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuitCommand implements Command {

    Player player;

    public QuitCommand() throws InterruptedException {
        System.out.println("The game is over. Here is your current game status: ");
        MapCommand.map();
        System. exit(0); //need to test if it closes the program or can still let user see the map stats
    }

    public boolean execute(Player player) {
        return true;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}