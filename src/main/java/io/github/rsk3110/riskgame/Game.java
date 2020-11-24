package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.controller.GameController;
import io.github.rsk3110.riskgame.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
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

    private List<Territory> territories;
    private Player currPlayer;
    private int currRound;
    private GameController gameController;

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
        if(AI){
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
        this.gameController = null;
    }

    /**
     * Initialize territory occupants and armies
     */
    public void init() {
        territories = new ArrayList<>(this.world.getGraph().vertexSet());
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

    public World getWorld() {
        return this.world;
    }

    public Player getCurrPlayer() { return this.currPlayer; }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

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

    private void AI(){
        int max = 0;
        Territory territoryAI = null;
        for(Territory t : currPlayer.getTerritories())
        {
            if(t.getArmies() > max) {
                max = t.getArmies();
                territoryAI = t; //highest army territory owned by AI player
            }
        }

        ArrayList<Territory> neighborT = new ArrayList<>();

        //finds neighbor
        for(Territory t : territories){
            if(territoryAI.isNeighbor(world, t)) {
                neighborT.add(t);
            }
        }

        Territory temp = territoryAI; //Just a to avoid setting it to null, if no neighboring territory has lower armies than AI's territory
        for(Territory t : neighborT)
        {
            if(t.getArmies() < territoryAI.getArmies()) {
                temp = t; //lowest army territory neighboring the AI players territory
            }
        }

        if(temp == territoryAI){
            gameController.skipTurn();
        }
        if(temp.isOccupiedBy(currPlayer)){
            gameController.fortify(territoryAI, temp, (territoryAI.getArmies() - 1));
        }
        else if(!temp.isOccupiedBy(currPlayer)){
            gameController.attack(territoryAI, temp, 1, promptForIntegerValue(1, temp.getArmies(), temp.getOccupant().getName() + ": How many dice would you like to roll?"));
        }
        else{
            gameController.skipTurn();
        }
    }

    /**
     * Prompts the player for a number within two specified values.
     *
     * @param min minimum number
     * @param max maximum number
     * @param prompt message to prompt player with
     * @return player's input
     */
    private static int promptForIntegerValue(int min, int max, String prompt) {
        Pattern pattern = Pattern.compile("\\d+");
        String userInput = null;
        int userNum;
        do {
            do {
                if(userInput != null) JOptionPane.showMessageDialog(null, "Invalid input. Must be number " + ((max == min) ? min : "between" + min + " and " + max + "."), "Invalid Input", JOptionPane.ERROR_MESSAGE);
                userInput = JOptionPane.showInputDialog(null, prompt + " " + ((max == min) ? min : min) + " to " + max + ".");
            } while(!pattern.matcher(userInput).matches());
            userNum = Integer.parseInt(userInput);
        } while(!(userNum >= min && userNum <= max));

        return userNum;
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
