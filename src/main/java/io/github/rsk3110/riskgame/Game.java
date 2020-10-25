package io.github.rsk3110.riskgame;

import java.nio.file.Paths;
import java.util.*;

public class Game {

    private CommandManager commandManager;
    private World world;
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

    public Game() {
        this.scanner = new Scanner(System.in);

        this.commandManager = new CommandManager();
        this.commandManager.register("help", new HelpCommand());
        this.commandManager.register("map", new MapCommand());
        this.commandManager.register("attack", new AttackCommand());
        this.commandManager.register("fortify", new FortifyCommand());
        this.commandManager.register("skip", new SkipCommand());
        this.commandManager.register("quit", new QuitCommand());

        WorldFileLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        this.world = loader.load("default");

        System.out.println("Welcome to RISK! How many players will be playing? (2-6)");
        System.out.print("> ");
        int numPlayers = getNumInRange(2, 6);
        this.players = new ArrayList<Player>(){{ //init players
            for(int i = 0; i < numPlayers; i++) {
                add(new Player(world, "player" + i, maxArmies.get(numPlayers)));
            }
        }};

        List<Territory> territories = new ArrayList<Territory>(world.getTerritoryMap().keySet());
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

    //Added an intro text which is basically the help section and win check after each play
    public void play() {
        for(;;) { //loop forever
            for(Player player : this.players) {
                if(player.getTerritories().size() == 0) continue; //skip turn if player eliminated.
                boolean end = false;
                player.updateArmies();
                System.out.println("It is now " + player.getName() + "'s turn.");
                doArmyAllocation(player);
                while(!end){ //while no command terminates turn
                    System.out.print("{" + player.getName() + "} > ");
                    end = this.commandManager.handleInput(player, this.scanner.nextLine());
                    if(checkIfOver())
                        commandManager.execute(player, "quit");
                }
            }
        }
    }

    private void parseAndAllocate(Player player, List<String> args) {
        if(args.size() != 2) {
            System.out.println("Invalid number of arguments. {<target> <#armies>}");
            return;
        }

        Territory target = Territory.idToTerritory(player, args.get(0));
        int numArmies;
        try {
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

    private void doArmyAllocation(Player player) {
        player.getTerritories().forEach(System.out::print);

        do {
            System.out.println("You have " + player.getArmies() + " armies to allocate. {<target> <#armies>}");
            System.out.print("{" + player.getName() + " allocating} > ");
            parseAndAllocate(player, Arrays.asList((new Scanner(System.in)).nextLine().split(" ")));
        } while(player.getArmies() > 0);
    }

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

    //checking if players won or don't have enough armies to play
    private boolean checkIfOver(){
        //checks if any player owns the whole world
        for (Player p: players){
            if ((p.getTerritories()).size() == 42){
                System.out.println(p.getName() + " wins.");
                return true;
            }
        }

        //checks if all the armies on each territory equals 1 (because users can't move armies when there is only 1 army on each territory)
        for(Territory t: world.getTerritoryMap().keySet()){
            if(t.getArmies() != 1) return false;
        }
        System.out.println("Nobody can move, it's a draw.");
        return true;
    }
}
