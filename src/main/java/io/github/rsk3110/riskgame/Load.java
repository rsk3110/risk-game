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
     * @return the object holding game state
     */
    public static Object loadGame(String fileName) throws Exception {
        File file = new File(fileName);
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
        Object gameState = input.readObject();
        input.close();

        return gameState;
    }
}
