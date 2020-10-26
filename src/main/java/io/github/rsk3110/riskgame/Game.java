package io.github.rsk3110.riskgame;

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
    private List<Player> players;

    static final private TreeMap<Integer, Integer> maxArmies = new TreeMap<Integer, Integer>(){{ // defines default army sizes per player sizes
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
     * Creates Game. Creates all the commands and loads the world.
     * Asks for the number of players, splits territories between players,
     * and randomizes army allocation to each territory
     */
    public Game() {
        this.commandManager = new CommandManager();
        this.commandManager.register("help", new HelpCommand());
        this.commandManager.register("map", new MapCommand());
        this.commandManager.register("attack", new AttackCommand());
        this.commandManager.register("fortify", new FortifyCommand());
        this.commandManager.register("skip", new SkipCommand());
        this.commandManager.register("quit", new QuitCommand());

        WorldFileLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        this.world = loader.load("default"); // load in level data

        System.out.println("Welcome to RISK! How many players will be playing? (2-6)");
        System.out.print("> ");
        int numPlayers = getNumInRange(2, 6);
        this.players = new ArrayList<Player>(){{ //init players
            for(int i = 0; i < numPlayers; i++) {
                add(new Player(world, "player" + i, maxArmies.get(numPlayers)));
            }
        }};

        List<Territory> territories = new ArrayList<>(world.getTerritoryMap().keySet());
        Collections.shuffle(territories);

        int index = 0;
        for(Territory territory : territories) { //assign territories
            territory.setOccupant(players.get(index));
            players.get(index).allocateArmies(1, territory);
            index = (index + 1) % players.size();
        }

        for(Territory territory : territories) { //randomly assign armies
            Player occupyingPlayer = territory.getOccupant();
            if (occupyingPlayer.getArmies() > 0) {
                final int minArmiesUsed = 1;
                final int maxArmiesUsed = occupyingPlayer.getArmies();
                final int armiesUsed = (int) Math.floor(minArmiesUsed + (maxArmiesUsed - minArmiesUsed) * Math.pow((new Random()).nextDouble(), 30));

                occupyingPlayer.setArmies(occupyingPlayer.getArmies() - armiesUsed);
                territory.setArmies(territory.getArmies() + armiesUsed);
            }
        }
    }

    /**
     * Runs loop till game ends or player quits game.
     */
    public void play() {
        for(;;) { //loop forever
            for(Player player : this.players) {
                if(player.getTerritories().size() == 0) continue; //skip turn if player eliminated.
                boolean end = false;
                player.updateArmies(); // Add rewarded armies
                System.out.println("It is now " + player.getName() + "'s turn.");
                runArmyAllocation(player); // Allow player to allocate free armies
                while(!end){ //while no command terminates turn
                    System.out.print("{" + player.getName() + "} > ");
                    end = this.commandManager.handleInput(player, (new Scanner(System.in)).nextLine()); // get next command
                    if(checkIfOver())
                        commandManager.execute(player, "quit");
                }
            }
        }
    }

    /**
     * Parses out player army allocation string and allocates armies to territory
     * if valid.
     *
     * @param player player executing the command
     * @param args input to parse
     */
    private void parseAndAllocate(Player player, List<String> args) {
        if(args.size() != 2) {
            System.out.println("Invalid number of arguments. {<target> <#armies>}");
            return;
        }

        Territory target = Territory.idToTerritory(player, args.get(0));
        int numArmies;
        try { // try-catch for if player does not input number
            numArmies = Integer.parseInt(args.get(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Try a new number of armies. {<target> <#armies>}");
            return;
        }

        if(target == null || !target.isOccupiedBy(player)) {
            System.out.println("target is not occupied by player or does not exist. {<target> <#armies>}");
            return;
        } else if(numArmies < 1 || numArmies > player.getArmies()) {
            System.out.println("Not enough armies to move. (1 to " + player.getArmies() + ") {<target> <#armies>}");
            return;
        }

        player.allocateArmies(numArmies, target);
    }

    /**
     * Lists player territories and prompts user to allocate player armies
     *
     * @param player player executing the command
     */
    private void runArmyAllocation(Player player) {
        player.getTerritories().forEach(System.out::print);

        do {
            System.out.println("You have " + player.getArmies() + " armies to allocate. {<target> <#armies>}");
            System.out.print("{" + player.getName() + " allocating} > ");
            parseAndAllocate(player, Arrays.asList((new Scanner(System.in)).nextLine().split(" ")));
        } while(player.getArmies() > 0);
    }

    /**
     * Prompt for input within specified range.
     *
     * @param min minimum value
     * @param max maximum value
     * @return resulting number
     */
    private int getNumInRange(int min, int max) {
        try {
            Scanner scanner = new Scanner(System.in);
            int num;
            do {
                num = scanner.nextInt();
                if(!(num >= min && num <= max)) System.out.println("Invalid number. Must be " + ((max == min) ? min : min + " to " + max + "."));
            } while(!(num >= min && num <= max));
            return num;
        } catch (Exception e) {
            System.out.println("Invalid Input. Try a new number.");
            return getNumInRange(min, max);
        }
    }

    /**
     * Checks if game draws or player won
     *
     * @return whether game has ended
     */
    private boolean checkIfOver(){
        for (Player p: players){
            if ((p.getTerritories()).size() == world.getTerritoryMap().keySet().size()){ // player owns all territories
                System.out.println(p.getName() + " wins.");
                return true;
            }
        }

        for(Territory t: world.getTerritoryMap().keySet()){
            if(t.getArmies() != 1) return false; // At least 1 territory can be played
        }
        System.out.println("Nobody can move, it's a draw.");
        return true;
    }
}
