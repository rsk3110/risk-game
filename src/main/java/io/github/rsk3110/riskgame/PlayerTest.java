package io.github.rsk3110.riskgame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private World world;
    private Player player = new Player(world, "Player", 2);
    private Territory territory = new Territory(1);
    private Territory territory2 = new Territory(2);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertEquals("Player",player.getName());
    }

    @Test
    public void addTerritory() {
        player.addTerritory(territory);
        assertEquals(1,player.getTerritories().size());
    }

    @Test
    public void add2Territory() {
        player.addTerritory(territory);
        player.addTerritory(territory2);
        assertEquals(2,player.getTerritories().size());
    }

    @Test
    public void removeTerritory() {
        player.addTerritory(territory);
        player.removeTerritory(territory);
        assertEquals(0,player.getTerritories().size());
    }

    @Test
    public void removeDifferentTerritory() {
        player.addTerritory(territory);
        player.removeTerritory(territory2);
        assertEquals(1,player.getTerritories().size());
    }

    @Test
    public void getTerritories() {
        player.addTerritory(territory);
        player.addTerritory(territory2);
        assertNotNull(player.getTerritories());
    }

    @Test
    public void isOccupying() {
        player.addTerritory(territory);
        assertTrue(player.isOccupying(territory));
    }

    @Test
    public void isNotOccupying() {
        player.addTerritory(territory);
        assertFalse(player.isOccupying(territory2));
    }

    @Test
    public void getArmies() {
        assertEquals(2,player.getArmies());
    }

    @Test
    public void setArmies() {
        player.setArmies(5);
        assertEquals(5,player.getArmies());
    }

    @Test
    public void allocateArmiesCheckPlayersUnallocatedArmies() {
        player.allocateArmies(1,territory);
        assertEquals(1,player.getArmies());
    }

    @Test
    public void allocateArmiesCheckTerritoriesNewNumberOfArmies() {
        player.allocateArmies(1,territory);
        assertEquals(1,territory.getArmies());
    }

    @Test
    public void allocateArmiesAddArmiesMultipleTimes() {
        player.allocateArmies(1,territory);
        player.allocateArmies(1,territory);
        assertEquals(2,territory.getArmies());
    }

    @Test
    public void allocateArmiesAddMoreArmiesThanPlayerHas() {
        player.allocateArmies(3, territory);
        assertEquals(-1, player.getArmies());
    }

    @Test
    public void getWorld() {
        assertEquals(world,player.getWorld());
    }
}