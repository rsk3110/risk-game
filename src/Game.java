package io.github.rsk3110.riskgame;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

    private CommandManager commandManager;
    private World world;
    private List<Continent> continents;
    private List<Territory> territories;
    private List<Player> players;
    private Scanner scanner;

    static final private int  NUM_PLAYERS = 2;
    static final private int  STARTING_ARMIES = 50;

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    public Game() {
        this.scanner = new Scanner(System.in);

        this.commandManager = new CommandManager();
        this.commandManager.register("help", new HelpCommand());
        this.commandManager.register("map", new MapCommand());
        this.commandManager.register("attack", new AttackCommand());
        this.commandManager.register("skip", new SkipCommand());
        //this.commandManager.register("skip", new quitCommand());

        this.world = (new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"))).load("default");
        this.continents = this.world.getContinents();
        this.territories = new ArrayList<Territory>(this.world.getGraph().vertexSet());
        Collections.shuffle(territories);

        this.players = new ArrayList<Player>(){{
            for(int i = 0; i < NUM_PLAYERS; i++) {
                add(new Player("player" + i));
            }
        }};

        int index = 0;
        for(Territory territory : this.territories) {
            this.players.get(index).addTerritory(territory);

            if(index == this.players.size() - 1) index = 0;
            else index++;
        }
    }

    //Added an intro text which is basically the help section and win check after each play
    public void play() {
        this.commandManager.handleInput(null, "help");//prints intro: all the rules and commands found in help

        for(;;) {
            for(Player player : this.players) {
                boolean end = false;
                while(!end){
                    end = this.commandManager.handleInput(player, this.scanner.nextLine());
                    if(win() == true){
                        this.commandManager.handleInput(null, "quit");
                    }
                }
            }
        }
    }

    //checking if players won or don't have enough armies to play
    private boolean win(){
        //checks if any player owns the whole continent
        for (Player p: players){
            if ((p.getTerritories()).size() == 42){
                System.out.println(p.getName() + " wins.");
                return true;
            }
        }
        int sumOfArmies = 0;
        //checks if all the armies on each territory equals 1 (because users can't move armies when there is only 1 army on each territory)
        for(Territory t: territories){
            sumOfArmies += t.getArmies();
        }
        if(sumOfArmies == 42){
            //this assumes that none of the territories are neutral
            //This still needs work to properly cover all edge cases
            System.out.println("The match is a draw.");
            return true;
        }
        return false;
    }
}
