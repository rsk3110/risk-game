import java.util.List;
import java.util.Random;

public class AttackCommand implements Command {

    private int player1Dice1;
    private int player1Dice2;
    private int player2Dice1;
    private int player2Dice2;

    public AttackCommand(){

    }

    public void execute() {
        attack();
    }

    private void attack(){

    }
    private static int rollDice(){
        // create instance of random class
        Random rand = new Random();

        // Generates random integers between 1 to 6
        return rand.nextInt(6) + 1;
    }

    public void execute(List<String> args) {

    }

    public static void main(String[] args) {
        //for(int i = 0; i < 100; i++){System.out.println(rollDice());}
    }
}
