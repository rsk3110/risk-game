
package io.github.rsk3110.riskgame;

import java.util.List;
/*
* Tells player how to play the game
* All commands and their descriptions
* Rules for attack
*
* */
public class HelpCommand implements Command {

    public HelpCommand(){

    }

    public boolean execute(Player player) {
        //Game intro
        System.out.println("You are playing Risk.");
        System.out.println(
                "There are 6 continents (no Antarctica) and 42 territories divided among them./n " +
                "There are two players (in this version) each with 21 territories./n" +
                "Each of the territories have 2 - 3 armies that add up to  a total of 50 armies per player./n");

        //Game commands
        System.out.println("There 5 commands that you can use: ");
        System.out.println("1. Map: shows the status of the world, which territory belong to which player and the number of armies.");
        System.out.println("2. Help: tells you how to play the game.");
        System.out.println("3. Skip: Skips your turn and goes to the next player.");
        System.out.println("4. Quit: Quits the program");
        System.out.println("5. Attack: Start by selecting your own territory. Then three different modes of play: /n" +
                " - 'Attack' a your own bordering territory - will result in moving armies from first territory to second./n" +
                " - Attack a neutral territory - will move armies from your territory to a neutral (unowned) territory./n" +
                " - Attack an opponent: You choose how many dice to roll and who ever has the smaller dice will lose one army./n");

        //Win conditions
        System.out.println("The that takes over the world is the winner.");
        System.out.println("Otherwise its a draw.");

        return false;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }
}
