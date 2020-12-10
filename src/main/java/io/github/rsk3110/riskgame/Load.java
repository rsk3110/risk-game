package io.github.rsk3110.riskgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

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
    public static Map<String, Object> loadGame(String fileName) {
        HashMap<String, Object> gameState = new HashMap<String, Object>(){{
            put("players", new Object());
            put("territories", new Object());
            put("currPlayers", 0);
            put("currRound", 0);
        }};
        try {
            File file = new File(fileName);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            gameState = (HashMap<String, Object>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
        }

        return gameState;
    }
}
