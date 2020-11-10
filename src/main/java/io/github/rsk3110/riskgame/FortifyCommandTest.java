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

    Territory t = new Territory(1);
    Territory t1 = new Territory(2);
    Territory t2 = new Territory(7);

    private FortifyCommand f = new FortifyCommand();
    private List<String> args = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        WorldFileLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        this.world = loader.load("default"); // load in level data
        territories = new ArrayList<>(world.getTerritoryMap().keySet());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void execute() {
        player = new Player (world, "Player", 2);
        assertFalse(f.execute(player));
    }

    @Test
    public void testExecuteCondition1() { //if Player enters 4 arguments
        player = new Player (world, "Player", 2);
        args.add("A");
        args.add("B");
        args.add("C");
        args.add("D");
        assertFalse(f.execute(player, args));
    }

    @Test
    public void testExecuteCondition2() { //if Player enters 2 arguments
        player = new Player (world, "Player", 2);
        args.add("A");
        args.add("B");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition3() { //Wrong number of armies
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        args.add("A");
        args.add("B");
        args.add("C");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition4() {//correct number of armies but not existing origin
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        args.add("A");
        args.add("B");
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition5() {//Correct armies and origin but not neighbouring target
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        args.add("1");
        args.add("7"); //not neighboring territory
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition6() {//Correct origin and neighboring target but target not occupied by player
        player = new Player (world, "Player", 2);
        player2 = new Player (world, "Player2", 2);
        t.setOccupant(player);
        t1.setOccupant(player2);
        args.add("1");
        args.add("2"); //target occupied by player 2
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If player enter the right number of arguments
    public void testExecuteCondition7() {//Correct origin and neighboring target and target occupied by player
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("2"); //target occupied by player 2
        args.add("1");
        assertFalse(f.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition8() { //Player tries to move too many armies (have to leave at least one army at origin)
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("2");
        args.add("2"); //player has only two armies but they are trying to move all of them
        assertFalse(f.execute(player, args));
    }

    @Test //If all args correct
    public void testExecuteCondition9() {
        player = new Player (world, "Player", 2);
        t.setOccupant(player);
        t1.setOccupant(player);
        args.add("1");
        args.add("2");
        args.add("1"); //player has only two armies but they are trying to move all of them
        assertTrue(f.execute(player, args));
    }

}