import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuitCommand implements Command {

    Player player;

    public QuitCommand() throws InterruptedException {
        System.out.println("You have decided to quit. The game will quit in 1 minute. Here is your current game status: ");
        MapCommand.map();
        TimeUnit.MINUTES.sleep(1);//Delay of 1 minutes so player can check their status
        System. exit(0);
    }

    public boolean execute(Player player) {
        return true;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}