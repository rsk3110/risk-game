package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.controller.GameController;
import io.github.rsk3110.riskgame.view.GameView;
import javafx.util.Pair;

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

    private final transient List<Consumer<Pair<Player, Integer>>> turnStartListeners;

    private World world;
    private List<Player> players;

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
    public Game(final World world, final int playerCount,int AI, boolean load) {

        if(load){
            loadState();
        } else {

            this.players = IntStream.range(0, playerCount - AI)
                    .mapToObj(i -> new Player(world, String.format("Player %d", i + 1), MAX_ARMIES.get(playerCount)))
                    .collect(Collectors.toList());
            for(; AI > 0 ; AI--) {
                players.add(new AI(world, String.format("AI Player %d", AI), MAX_ARMIES.get(playerCount)));
            }
            this.territories = new ArrayList<Territory>();
            this.currPlayer = players.get(0);
        }
        this.world = world;
        this.turnStartListeners = new ArrayList<>();
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
        this.turnStartListeners.forEach(l -> l.accept(new Pair<Player, Integer>(this.currPlayer, this.currRound)));
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

    /**
     * Iterates to next player. Adds to round count once last player is passed,
     * and calls AI play() method when it is an AI's turn.
     */
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
        this.notifyTurnListeners();
        if(this.currPlayer.getClass() == AI.class){
            ((AI)currPlayer).play(gameController);
            nextTurn();
        }
    }
    
    public List<Player> getPlayers() {
        return this.players;
    }

    public void addTurnStartListener(final Consumer<Pair<Player, Integer>> listener) {
        this.turnStartListeners.add(listener);
    }

    /**
     * Allocates bonus armies to player
     * @param target territory to allocate to
     * @param count number of armies to allocate
     */
    public void allocateBonusArmies(Territory target, int count) {
        currPlayer.allocateArmies(count, target);
    }

    public Map<String, Object> getCurrentState(){
        HashMap<String, Object> gameState = new HashMap<>();

        gameState.put("players", players);
        gameState.put("territories", territories);
        gameState.put("currPlayer", currPlayer);
        gameState.put("currRound", currRound);

        return gameState;
    }

    public void loadState() {
        Map<String, Object> gameState = Load.loadGame("savedGame");

        Object playersBlob = gameState.get("players");
        if(playersBlob instanceof ArrayList) {
            this.players = (ArrayList<Player>) playersBlob;
        } else {
            return;
        }
        Object territoriesBlob = gameState.get("territories");
        if(territoriesBlob instanceof ArrayList) {
            this.territories = (ArrayList<Territory>) territoriesBlob;
        } else {
            return;
        }
        Object currPlayerBlob = gameState.get("currPlayer");
        if(currPlayerBlob instanceof Player) {
            this.currPlayer = (Player) currPlayerBlob;
        } else {
            return;
        }
        Object currRoundBlob = gameState.get("currRound");
        if(currRoundBlob instanceof Integer) {
            this.currRound = (int) currRoundBlob;
        } else {
            return;
        }
    }

    /**
     * Initializes army allocation map
     */
    static {
        MAX_ARMIES = new HashMap<>();
        MAX_ARMIES.put(2, 50);
        MAX_ARMIES.put(3, 35);
        MAX_ARMIES.put(4, 30);
        MAX_ARMIES.put(5, 25);
        MAX_ARMIES.put(6, 20);
    }
}
