package io.github.rsk3110.riskgame;

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

    Territory t1;
    Territory t2;
    Territory t3;
    Territory t4;

    @Before
    public void setUp() throws Exception {
        WorldFileLoader loader = new WorldFileLoader(Paths.get("").toAbsolutePath().resolve("worlds"));
        this.world = loader.load("default"); // load in level data
        territories = new ArrayList<>(world.getTerritoryMap().keySet());

        player = new Player (world, "Player", 2);
        player2 = new Player (world, "Player2", 2);

        Territory t1 = Territory.nameToTerritory(player,"Alberta");
        Territory t2 = Territory.nameToTerritory(player,"Ontario");
        Territory t3 = Territory.nameToTerritory(player2,"Alaska");
        Territory t4 = Territory.nameToTerritory(player2,"Northwest Territory");

        t1.setOccupant(player);
        t2.setOccupant(player2);
        t3.setOccupant(player2);
        t4.setOccupant(player2);

        t1.setArmies(8);
        t2.setArmies(8);
        t3.setArmies(8);
        t2.setArmies(1);
    }

    @Test
    public void testValidAttack() {
        assertTrue(AttackCommand.execute(player, t1, t2, 1,1));
    }

    @Test
    public void testInvalidArmySize() {
        assertFalse(AttackCommand.execute(player2, t4, t1, 1,1));
    }

    @Test
    public void testNotNeighbor() {
        assertFalse(AttackCommand.execute(player, t1, t3, 1,1));
    }

    @Test
    public void testNotOccupant() {
        assertTrue(AttackCommand.execute(player, t2, t1, 1,1));
    }
}