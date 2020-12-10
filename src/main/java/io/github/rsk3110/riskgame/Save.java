package io.github.rsk3110.riskgame;
import java.io.*;
import java.util.ArrayList;

/**
 * Saves game
 *
 * @author Tooba Sheikh
 **/
public class Save{

    private static ArrayList gameS;

    public Save(){
        gameS = new ArrayList<>();
    }

    /**
     * Saves current Game state into a file using serializable
     *
     * @param game current game
     * @return returns false so that game does not go to next turn
     */
    public static boolean execute(Game game) throws Exception {
        gameS = game.getCurrentState();
        saveGame(gameS,"savedGame");
        return false; // returns false so that game does not go to next turn
    }

    /**
     * Saves current Game state into a file using serializable
     *
     * @param gameState an object that holds every serializable object
     * @param fileName is the name of the file that we want to save as
     */
    public static void saveGame(Serializable gameState, String fileName) throws Exception {
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(gameState);
        out.close();
    }

}