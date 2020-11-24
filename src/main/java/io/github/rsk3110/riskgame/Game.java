package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.view.GameView;

import java.awt.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Runs the Game
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 * @author Mark Johnson
 **/
public class Game {
    private static final Map<Integer, Integer> MAX_ARMIES; // defines default army sizes per player sizes

    private final transient List<Consumer<Player>> turnStartListeners;

    private final World world;
    private final List<Player> players;

    private Player currPlayer;
    private int currRound;

    public static void main(String[] args) {
        final WorldLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        EventQueue.invokeLater(() -> new GameView(loader));
    }

    /**
     * Creates a Game object with a given world and player count.
     *
     * @param world World to use to initialize game
     * @param playerCount number of players
     */
    public Game(final World world, final int playerCount, final boolean AI) {
        this.world = world;
        if(AI == true){
            this.players = IntStream.range(0, playerCount)
                .mapToObj(i -> new Player(world, String.format("AI Player %d", i + 1), MAX_ARMIES.get(playerCount)))
                .collect(Collectors.toList());
            players.get(0).setName("Player 1");
        }
        else {
            this.players = IntStream.range(0, playerCount)
                    .mapToObj(i -> new Player(world, String.format("Player %d", i + 1), MAX_ARMIES.get(playerCount)))
                    .collect(Collectors.toList());
        }
        this.currPlayer = players.get(0);
        this.turnStartListeners = new ArrayList<>();
        this.currRound = 0;
    }

    /**
     * Initialize territory occupants and armies
     */
    public void init() {
        List<Territory> territories = new ArrayList<>(this.world.getGraph().vertexSet());
        Collections.shuffle(territories);

        int index = 0;
        for (Territory territory : territories) { //assign territories
            territory.setOccupant(players.get(index));
            players.get(index).allocateArmies(1, territory);
            index = (index + 1) % players.size();
        }

        while (this.players.stream().mapToInt(Player::getArmies).sum() > 0) {
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

        this.notifyTurnListeners();
    }

    private void notifyTurnListeners() {
        this.turnStartListeners.forEach(l -> l.accept(this.currPlayer));
    }

    public void quitGame() {
        System.exit(0);
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

        Territory target = Territory.nameToTerritory(player, args.get(0));
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

    public World getWorld() {
        return this.world;
    }

    public Player getCurrPlayer() { return this.currPlayer; }

    public void nextTurn() {
        int currIndex = players.indexOf(currPlayer);
        Player nextPlayer;
        if(currIndex == players.size() - 1) {
            nextPlayer = players.get(0);
            currRound++;
        } else
            nextPlayer = players.get(currIndex + 1);
        currPlayer = nextPlayer;
        if(currRound != 0) currPlayer.updateArmies();
        if(this.currPlayer.getName().contains("AI")){
            AI();
            nextTurn();
        }
    }
    
    public List<Player> getPlayers() {
        return this.players;
    }

    public void addTurnStartListener(final Consumer<Player> listener) {
        this.turnStartListeners.add(listener);
    }

    public void allocateBonusArmies(Territory target) {
        currPlayer.updateArmies();
    }

    private void AI() {
        System.out.println("AIIIIIIII");
    }

    static {
        MAX_ARMIES = new HashMap<>();
        MAX_ARMIES.put(2, 50);
        MAX_ARMIES.put(3, 35);
        MAX_ARMIES.put(4, 30);
        MAX_ARMIES.put(5, 25);
        MAX_ARMIES.put(6, 20);
    }
}
