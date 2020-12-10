package io.github.rsk3110.riskgame;

import java.io.*;

/**
 * Loads game
 *
 * @author Tooba Sheikh
 **/
public class Load {

    public Load(){};

    /**
     * Loads game state by deserializing the saved file
     *
     * @param fileName file to deserialize
     * @return the obj holding game state
     */
    public static Object loadGame(String fileName) throws Exception {
        File file = new File(fileName);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

        // Deserialize the object
        Object gameState = in.readObject();
        in.close();

        return gameState;
    }
}
