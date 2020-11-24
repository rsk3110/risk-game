package io.github.rsk3110.riskgame;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class FortifyCommandTest {

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
        t2.setOccupant(player);
        t3.setOccupant(player);
        t4.setOccupant(player2);

        t1.setArmies(8);
        t2.setArmies(8);
        t3.setArmies(8);
        t2.setArmies(1);
    }

    @Test
    public void testValidFortify() {
        assertFalse(FortifyCommand.execute(player, t1, t2,1));
    }

    @Test
    public void testNotNeighbor() {
        assertFalse(FortifyCommand.execute(player, t1, t3,1));
    }

    @Test
    public void testNotOccupant() {
        assertFalse(FortifyCommand.execute(player, t1, t4,1));
        assertFalse(FortifyCommand.execute(player, t4, t1,1));
    }
}