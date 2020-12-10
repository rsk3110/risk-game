package io.github.rsk3110.riskgame;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Saves game
 *
 * @author Tooba Sheikh
 **/
public class Save{

    private static Map<String, Object> gameS;

    public Save(){
        //gameS = new HashMap<>();
    }

    /**
     * Saves current Game state into a file using serializable
     *
     * @param game current game
     * @return returns false so that game does not go to next turn
     */
    public static boolean execute(Game game) {
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
    public static void saveGame(Map<String, Object> gameState, String fileName) {
        try {
            ObjectOutput output = new ObjectOutputStream(new FileOutputStream(fileName));
            output.writeObject(gameState);
            output.close();
        } catch (IOException e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, "Did not save.");
        }
    }

}