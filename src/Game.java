import java.util.*;

public class Game {

    private CommandManager cmdMgr;
    private LinkedList<Player> players;
    private Player currPlayer;

    public static void main(String[] args) {
        Game game = new Game();
    }

    public Game() {
        this.cmdMgr = new CommandManager();
        this.cmdMgr.register("help", new HelpCommand());
        this.cmdMgr.register("map", new MapCommand());
        this.cmdMgr.register("attack", new AttackCommand());
        this.cmdMgr.register("skip", new SkipCommand());

        this.players = new LinkedList<>();
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        //while (! finished) {
            //Command command = parser.getCommand();
            //finished = execute(command);
        //}
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void getcurrPlayer(){

    }
}
