package io.github.rsk3110.riskgame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FortifyCommandTest {

    private World world;
    private List<Territory> territories;

    private Player player;
    private Player player2;

    Territory t;
    Territory t1;
    Territory t2;

    private FortifyCommand f = new FortifyCommand();
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

        System.out.println(t.getName());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void execute() {
        assertFalse(f.execute(player));
    }

    @Test
    public void testExecuteCondition1() { //if Player enters 4 arguments
        args.add("A");
        args.add("B");
        args.add("C");
        args.add("D");
        assertFalse(f.execute(player, args));
    }

    @Test
    public void testExecuteCondition2() { //if Player enters 2 arguments
        args.add("A");
        args.add("B");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition3() { //Wrong number of armies
        args.add("A");
        args.add("B");
        args.add("C");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition4() {//correct number of armies but not existing origin
        args.add("A");
        args.add("B");
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition5() {//Correct armies and origin but not neighbouring target
       // t.setOccupant(player);
        args.add("1");
        args.add("7"); //not neighboring territory
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition6() {//Correct origin and neighboring target but target not occupied by player
        args.add("1");
        args.add("2"); //target occupied by player 2
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition7() {//Correct origin and neighboring target and target occupied by player
        args.add("1");
        args.add("2"); //target occupied by player 2
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition8() { //Player tries to move too many armies (have to leave at least one army at origin)
        args.add("1");
        args.add("2");
        args.add("2"); //player has only two armies but they are trying to move all of them
        assertFalse(f.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition9() {
        args.add("1");
        args.add("2");
        args.add("1"); //player has only two armies but they are trying to move all of them
        assertTrue(f.execute(player, args));
    }

}