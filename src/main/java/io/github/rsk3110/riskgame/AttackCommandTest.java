package io.github.rsk3110.riskgame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AttackCommandTest {

    private World world;
    private List<Territory> territories;

    private Player player;
    private Player player2;

    Territory t;
    Territory t1;
    Territory t2;

    private AttackCommand a = new AttackCommand();
    private List<String> args = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        WorldFileLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        this.world = loader.load("default"); // load in level data
        territories = new ArrayList<>(world.getTerritoryMap().keySet());

        player = new Player (world, "Player", 2);
        player2 = new Player (world, "Player2", 2);

        Territory t = Territory.nameToTerritory(player,"1");
        Territory t1 = Territory.nameToTerritory(player,"2");
        Territory t2 = Territory.nameToTerritory(player2,"9");
        Territory t3 = Territory.nameToTerritory(player2,"3");

        t.setOccupant(player);
        t1.setOccupant(player);
        t2.setOccupant(player2);
        t3.setOccupant(player2);

        t.setArmies(1);
        t1.setArmies(8);
        t2.setArmies(8);
        t3.setArmies(1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void execute() {
        assertFalse(a.execute(player));
    }

    @Test
    public void testExecuteCondition1() { //if Player enters 3 arguments
        args.add("A");
        args.add("B");
        args.add("C");
        assertFalse(a.execute(player, args));
    }

    @Test //if Player enters 2 arguments (correct amount)
    public void testExecuteCondition2() { //the args are invalid
        args.add("A"); //origin
        args.add("B"); //target
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition3() { //Invalid origin
        args.add("B");
        args.add("3");
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition4() {//invalid target
        args.add("1");
        args.add("9");
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition5() {//valid target and valid origin but not enough armies at origin
        args.add("1");
        args.add("1");
        assertFalse(a.execute(player, args));
    }

}