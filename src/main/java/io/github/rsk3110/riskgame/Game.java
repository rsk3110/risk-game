package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.loader.WorldFileLoader;

import java.nio.file.Paths;
import java.util.*;

/**
 * Runs the Game
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/
public class Game {

    private CommandManager commandManager;
    private World world;
    private List<Continent> continents;
    private Set<Territory> territories;
    private List<Player> players;
    private Scanner scanner;

    static final private TreeMap<Integer, Integer> maxArmies = new TreeMap<Integer, Integer>(){{
        put(2, 50);
        put(3, 35);
        put(4, 30);
        put(5, 25);
        put(6, 20);
    }};

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    /**
     * Creates Game. Creates all the commands, loads the world.
     * Asks for the number of players, and depending on player input splits
     * territories between number of player and distributes number of armies on each territory
     */
    public Game() {
        this.scanner = new Scanner(System.in);

        this.commandManager = new CommandManager();
        this.commandManager.register("help", new HelpCommand());
        this.commandManager.register("map", new MapCommand());
        this.commandManager.register("attack", new AttackCommand());
        this.commandManager.register("skip", new SkipCommand());
        this.commandManager.register("quit", new QuitCommand());

        this.world = (new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"))).load("default");
        this.territories = world.getTerritoryMap().keySet();


        System.out.println("Welcome to RISK! How many players will be playing? (2-6)");
        int numPlayers = getNumPlayers();
        this.players = new ArrayList<Player>(){{
            for(int i = 0; i < numPlayers; i++) {
                add(new Player(world, "player" + i, maxArmies.get(numPlayers)));
            }
        }};

        int index = 0;
        for(Territory territory : territories) {
            territory.setOccupant(players.get(index));
            index = (index + 1) % players.size();
        }
    }

    /**
     * Runs loop till user enters command quit or game ends by checking if player won after each turn
     */
    public void play() {
        for(;;) { //loop forever
            for(Player player : this.players) {
                boolean end = false;

                System.out.println("It is now " + player.getName() + "'s turn.");
                while(!end){ //while no command terminates turn
                    System.out.print("> ");
                    end = this.commandManager.handleInput(player, this.scanner.nextLine());
//                    if(win() == true){
//                        this.commandManager.handleInput(null, "quit");
//                    }
                }
            }
        }
    }

    /**
     * Ask user to enter number of players and check if entered number of players valid
     *
     * @return returns the number of player
     */
    private int getNumPlayers() {
        try {
            Scanner scanner = new Scanner(System.in);
            int num;
            do {
                System.out.print("> ");
                num = scanner.nextInt();
                if(!(num >= 2 && num <= 6)) System.out.println("Invalid number. Must be from 2 to 6.");
            } while(!(num >= 2 && num <= 6));
            return num;
        } catch (Exception e) {
            System.out.println("Invalid Input. Try a number from 2 to 6.");
            return getNumPlayers();
        }
    }

    /**
     * Checks if game draws or player won
     *
     * @return true if player won or game draws
     */
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
