import java.util.*;

public class Game {

    private CommandManager cmdMgr;
    private Player player;

    public static void main(String[] args) {
        Game game = new Game();
    }

    public Game() {
        this.cmdMgr = new CommandManager();
        this.cmdMgr.register("help", new HelpCommand());
        this.cmdMgr.register("map", new MapCommand());
        this.cmdMgr.register("attack", new AttackCommand());
        this.cmdMgr.register("skip", new SkipCommand());

        this.player = new Player("", new ArrayList<Territory>());
    }
}
