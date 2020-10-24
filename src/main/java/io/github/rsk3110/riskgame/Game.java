package io.github.rsk3110.riskgame;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Game {

    private CommandManager cmdMgr;
    private Map<String, Continent> continents;
    private Map<String, Territory> territories;
    private List<Player> players;

    public static void main(String[] args) {
        Game game = new Game();
    }

    public Game() {
        this.cmdMgr = new CommandManager();
        this.cmdMgr.register("help", new HelpCommand());
        this.cmdMgr.register("map", new MapCommand());
        this.cmdMgr.register("attack", new AttackCommand());
        this.cmdMgr.register("skip", new SkipCommand());

        this.players = new ArrayList<Player>();
        this.players.add(new Player(""));
        for (Player player : players) {
            player.addTerritory(new Territory("", new Continent("")));
        }
    }
}
