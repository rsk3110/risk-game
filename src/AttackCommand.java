import java.util.List;
import java.util.Random;
import java.util.Scanner;
/*
* @ Tooba Sheikh
* Rules for Attack command. Player keeps attacking till player chooses to skip or loses a battle.
* Players can't attack with just 1 army on their territory.
* If player attacks a friendly(their own) territory, the armies from territory 1 move to territory 2.
* If player attacks a opposing player territory, the user will decide how many dice they want to roll.
* The territory with the the lower value dice will lose 1 army
* If dice are equal then both territories lose one army
* If current player's army on their own territory go below 1, the territory will be lost and will become neutral. (no neutral territory to start with)
*  - current players turn will also end
* If opposing player's territory will go below 1, the territory will be captured, the current player's army will move to new territory.
* Neutral territory with armies = 0, can be captured.
*
* Any boolean values returned in the execute function if its still the current players turn or next players turn.
*  True = next players turn
*  false = current players turn
* */
public class AttackCommand implements Command {

    public AttackCommand(){
    }

    private static int rollDice(){
        // create instance of random class
        Random rand = new Random();

        // Generates random integers between 1 to 6
        return rand.nextInt(6) + 1;
    }

    public boolean execute(Player player) {
        Player currPlayer = player;
        Territory myTerritory;
        Territory otherTerritory;

        MapCommand.map(); //need to print state of map

        //Get user input and recursively check if user entered an valid Territory
         myTerritory = playerTerritoryInput(currPlayer);

        //Get user input  for enemy territory and recursively check if user entered an valid Territory
        otherTerritory = otherTerritoryInput(myTerritory);

        //checking if chosen other territory is actually a friendly bordering territory
        for(Territory t : currPlayer.getTerritories()){

            //if the other territory belongs to current player than, armies from one territory will be moved to the other.
            if (t == otherTerritory){

                int armies = myTerritory.getArmies() - 1;
                otherTerritory.setArmies(otherTerritory.getArmies() + armies);
                myTerritory.setArmies(1);

                //Moved armies to a friendly territory, end turn
                return false;
            }
        }

        //if other territory is neutral
        if(otherTerritory.getArmies() < 1){

            int armies = myTerritory.getArmies() - 1;
            otherTerritory.setArmies(armies);
            myTerritory.setArmies(1);

            currPlayer.addTerritory(otherTerritory);

            //Captured a neutral territory, end turn
            return false;
        }

        //if attacking a not friendly territory
        boolean fail;

        //in the game one or two die are rolled depending on the user input (attack once or attack twice)
        int num = playerDiceInput();
        for (int i = 0; i <= num; i++){
            fail = attack(currPlayer, myTerritory, otherTerritory);
            if (fail){ // if current players lost the territory, then end turn and new players turn.
                return true;
            }
        }
         return false;
    }

    private boolean attack(Player player, Territory myTerritory, Territory enemyTerritory){
        int currPlayerDice = rollDice();
        int opposingPlayerDice = rollDice();

        System.out.print("You rolled: "+ currPlayerDice);
        System.out.print("Enemy rolled: "+ opposingPlayerDice);

        if(currPlayerDice < opposingPlayerDice){
            myTerritory.setArmies(myTerritory.getArmies() - 1); //Lose one army
            if(myTerritory.getArmies() < 1){
                System.out.println("Your attack failed.");
                player.removeTerritory(myTerritory);
                myTerritory.setArmies(0);
                return true;
            }
        }
        if(currPlayerDice > opposingPlayerDice){
            enemyTerritory.setArmies(enemyTerritory.getArmies() - 1); //Enemy Loses one army
            if(enemyTerritory.getArmies() < 1){
                System.out.println("Your attack succeeded. " + enemyTerritory.getName() +" belongs to you now.");
                player.addTerritory(enemyTerritory);
                enemyTerritory.setArmies(myTerritory.getArmies());
                myTerritory.setArmies(1);
                return false;
            }
        }
        if(currPlayerDice == opposingPlayerDice){
            myTerritory.setArmies(myTerritory.getArmies() - 1); //Player loses one army
            enemyTerritory.setArmies(enemyTerritory.getArmies() - 1); //Enemy Loses one army

            if((enemyTerritory.getArmies() < 1) && (myTerritory.getArmies() > 1)){
                System.out.println("Your attack succeeded. " + enemyTerritory.getName() +" belongs to you now.");
                player.addTerritory(enemyTerritory);
                enemyTerritory.setArmies(myTerritory.getArmies());
                myTerritory.setArmies(1);
                return false;
            }
            if(myTerritory.getArmies() < 1){
                System.out.println("Your attack failed.");
                player.removeTerritory(myTerritory);
                myTerritory.setArmies(0);
                return true;
            }
        }
        return false;
    }

    //Gets user input
    private String userInput(String userPrompt){
        Scanner sc = new Scanner(System.in);
        System.out.println(userPrompt);
        String userInput =  sc.nextLine();

        return userInput;
    }

    /*
    * Function to check user input is valid for their own territory
    */
    private Territory playerTerritoryInput(Player player){
        String myTerritory =  userInput("Type in the name of the territory to move from: ");

        //For loop checks if the territory belongs to the user
        //if territory belongs to user, then return the territory
        for(Territory t : player.getTerritories()){
            if (t.getName().equals(myTerritory)){
                if(t.getArmies() <=1 ){
                    //not enough armies to attack with
                    System.out.println("Invalid Territory, need to have more than one army on the territory to move forward");
                    t = playerTerritoryInput(player);
                }
                return t;
            }
        }

        //if territory invalid, then ask user to enter territory again and recursively keep going till user enters valid territory
        System.out.println("Invalid Territory");
        Territory t;
        t = playerTerritoryInput(player);

        return t;
    }

    private Territory otherTerritoryInput(Territory playersTerritory){

        String enemyTerritory =  userInput("Type in the name of the territory to move armies to: ");

        //For loop checks if the territory borders the users territory
        //if territory is a bordering territory, then return the territory
        for(Territory t : playersTerritory.getBorderTerritories()){
            if (t.getName().equals(enemyTerritory)){
                return t;
            }
        }

        //if territory invalid, then ask user to enter territory again and recursively keep going till user enters valid territory
        System.out.println("Invalid Territory");
        Territory t;
        t = otherTerritoryInput(playersTerritory);

        return t;
    }

    //Function to check user number of dice input
    private int playerDiceInput(){

        String str =  userInput("Type in the number of dice to attack with: ");
        int num =Integer.parseInt(str);

        if ((num >= 1) && (num <= 2)){
                return num;
        }
        //if number of dice is invalid
        System.out.println("Invalid number");
        num = playerDiceInput();

        return num;
    }

    public boolean execute(Player player, List<String> args) {
        return false;
    }

    public static void main(String[] args) {
    }
}
