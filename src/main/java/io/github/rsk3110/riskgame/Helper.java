package io.github.rsk3110.riskgame;

import javax.swing.*;
import java.util.regex.Pattern;

public class Helper {

    /**
     * Prompts the player for a number within two specified values.
     *
     * @param min minimum number
     * @param max maximum number
     * @param prompt message to prompt player with
     * @return player's input
     */
    public static int promptForIntegerValue(int min, int max, String prompt) {
        Pattern pattern = Pattern.compile("\\d+");
        String userInput = null;
        int userNum;
        do {
            do {
                if(userInput != null) JOptionPane.showMessageDialog(null, "Invalid input. Must be number " + ((max == min) ? min : "between " + min + " and " + max + "."), "Invalid Input", JOptionPane.ERROR_MESSAGE);
                userInput = JOptionPane.showInputDialog(null, prompt + " " + ((max == min) ? min : min) + " to " + max + ".");
            } while(!pattern.matcher(userInput).matches());
            userNum = Integer.parseInt(userInput);
        } while(!(userNum >= min && userNum <= max));

        return userNum;
    }
}
