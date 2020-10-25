package io.github.rsk3110.riskgame;

import java.util.*;

/**
 * Rules for Attack command.
 * If player attacks a friendly(their own) territory, the armies from territory 1 move to territory 2. // MOVING TO REINFORCE
 * If player attacks a opposing player territory, the user will decide how many dice they want to roll.
 * Territory must have (#dice + 1) armies to roll that amount of dice;
 * The territory that loses a confrontation will lose 1 army (#confrontations = minimum #dice of attacker and defender, defender wins on tie)
 * If defender's army drops to, attacker claims target territory and moves (#dice) armies from origin to target.
 *
 * @author Tooba Sheikh
 * @author Kaue Gomes e Sousa de Oliveira
 **/
public class AttackCommand implements Command {

    Player player;

    public AttackCommand(){
    }

    private Territory stringToTerritory(String str) {
        List<Object> territory = Arrays.asList(player.getWorld().getTerritoryMap().keySet().stream()
                .filter(t -> t.getName().equals(str)).toArray());
        return (territory.size() == 1) ? (Territory)territory.get(0) : null;
    }

    public boolean execute(Player player, List<String> args) {
        if(args.size() != 2) {
            System.out.println("Invalid number of arguments. {attack <origin> <target>}");
            return false;
        }

        this.player = player;
        Territory origin = stringToTerritory(args.get(0));
        Territory target = stringToTerritory(args.get(1));

        if (origin == null || !origin.isOccupiedBy(player)) {
            System.out.println("origin not owned by player or does not exist. {attack <origin> <target>}");
            return false;
        } else if (target == null || !origin.isNeighbor(player.getWorld(), target)) {
            System.out.println("target is not bordering origin or does not exist. {attack <origin> <target>}");
            return false;
        } else if (origin.getArmies() <= 1 ) {
            System.out.println("origin has insufficient armies. Must be greater than 1. {attack <origin> <target>}");
            return false;
        }

        if(target.isOccupiedBy(player)) {
            System.out.println("Can't attack own territory. Try fortify? {attack <origin> <target>}");
            return false;
        }
        else
            return attack(origin, target);
    }

    private int getNumDice(int max) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Attack with how many dice?");
            int num;
            do {
                num = scanner.nextInt();
                if(!(num > 0 && num <= max)) System.out.println("Invalid number. Must be 1 to " + max + ".");
            } while(!(num > 0 && num <= max));
            return num;
        } catch (Exception e) {
            System.out.println("Invalid Input. Try a new number of die.");
            return getNumDice(max);
        }
    }

    private List<Integer> getDiceValues(int max) {
        return new ArrayList<Integer>(){{
            Random random = new Random();
            for(int i = getNumDice(max); i > 0; i--) {
                int num = random.nextInt(6) + 1;
                add(num);
                System.out.println("You rolled: " + num);
            }
        }};
    }

    private boolean attack(Territory origin, Territory target){
        List<Integer> playerValues = getDiceValues(Math.min(3, origin.getArmies() - 1));
        List<Integer> targetValues = getDiceValues(Math.min(2, target.getArmies() - 1));
        int minDie = Math.min(playerValues.size(), targetValues.size());
        for(int i = minDie; i > 0; i--) {
            Integer playerValue = Collections.max(playerValues);
            playerValues.remove(playerValue);
            Integer targetValue = Collections.max(targetValues);
            targetValues.remove(targetValue);

            if(playerValue < targetValue || playerValue.equals(targetValue)) {
                origin.decrementArmies();
            } else {
                target.decrementArmies();
                if(target.getArmies() == 0) { //if defeated
                    target.setOccupant(player);
                    origin.moveArmy(playerValues.size(), target);
                    System.out.println("Success! You captured " + target.getName() + " and it now holds " + playerValues.size() + " armies.");
                    return false;
                }
            }
        }

        return false;
    }

    public boolean execute(Player player) {
        System.out.println("Invalid arguments. {attack <origin> <target>}");
        return false;
    }
}
