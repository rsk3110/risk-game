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
        Territory t2 = Territory.nameToTerritory(player,"3");
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
    public void testExecuteCondition2() { ;
        args.add("A");
        args.add("B");
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition3() { //Wrong number of armies
        t.setOccupant(player);
        args.add("A");
        args.add("B");
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition4() {//correct number of armies but not existing target
        t.setOccupant(player);
        args.add("A");
        args.add("1");
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition5() {//Correct armies and target but not neighbouring target
        t.setOccupant(player);
        args.add("8");
        args.add("1"); //not neighboring territory
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition6() {//Correct origin and neighboring target but target not occupied by player
        t.setOccupant(player);
        t1.setOccupant(player2);
        args.add("1");
        args.add("1"); //target occupied by player 2
        assertFalse(a.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition7() {//Correct origin and neighboring target and target occupied by player
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("1"); //target occupied by player 2
        assertFalse(a.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition8() { //Player tries to move too many armies (have to leave at least one army at origin)
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("1");
        assertFalse(a.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition9() {
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("1");
        assertTrue(a.execute(player, args));
    }
}