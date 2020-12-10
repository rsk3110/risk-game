package io.github.rsk3110.riskgame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test Territory class
 *
 * @author Tooba Sheikh
 **/
public class TerritoryTest {

    private World world;
    private Player player = new Player(world, "Player", 2);
    private Player player2 = new Player(world, "Player2", 1);
    private Territory territory = new Territory(1);
    private Territory target = new Territory(2);
    private Continent c = new Continent("A", null, 5);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setNameCheckForNull() {
       territory.setName("England");
       assertNotEquals(territory.getName(), null);
    }

    @Test
    public void getName() {
        territory.setName("England");
        assertEquals("England",territory.getName());
    }

    @Test
    public void setArmies() {
        territory.setArmies(6);
        assertEquals(6, territory.getArmies());
    }

    @Test
    public void getArmies() {
        territory.setArmies(6);
        assertEquals(6, territory.getArmies());
    }

    @Test
    public void checkIfRightNumberOfArmiesMoveToTarget() {
        territory.setArmies(6);
        target.setArmies(1);
        territory.moveArmy(3,target);
        assertEquals(4,target.getArmies());
    }

    @Test
    public void checkIfRightNumberOfArmiesLeftInTerritory() {
        territory.setArmies(6);
        target.setArmies(1);
        territory.moveArmy(3,target);
        assertEquals(3,territory.getArmies());
    }

    @Test
    public void checkIfAllArmiesMoveToTarget() {
        territory.setArmies(6);
        target.setArmies(1);
        territory.moveArmy(6,target);
        assertEquals(0,territory.getArmies());
    }

    @Test
    public void checkIfMoreThanMaxArmiesMoveToTarget() {
        territory.setArmies(6);
        target.setArmies(1);
        territory.moveArmy(7,target);
        assertEquals(-1,territory.getArmies());
    }

    @Test
    public void decrementArmiesBy1() {
        territory.setArmies(6);
        territory.decrementArmies();
        assertEquals(5, territory.getArmies());
    }

    @Test
    public void decrementArmiesTo0() {
        territory.setArmies(1);
        territory.decrementArmies();
        assertEquals(0, territory.getArmies());
    }

    @Test
    public void decrementArmiesBelow0() {
        territory.setArmies(0);
        territory.decrementArmies();
        assertEquals(0, territory.getArmies());
    }

    @Test
    public void setContinent() {
        territory.setContinent(c);
        assertEquals(c,territory.getContinent());
    }

    @Test
    public void getContinent() {
        territory.setContinent(c);
        assertEquals(c,territory.getContinent());
    }

    @Test
    public void setOccupant() {
        territory.setOccupant(player);
        assertEquals(player, territory.getOccupant());
    }

    @Test
    public void setOccupantForAnAlreadyOccupiedTerritory() {
        territory.setOccupant(player);
        territory.setOccupant(player2);
        assertEquals(player2, territory.getOccupant());
    }

    @Test
    public void getOccupant() {
        territory.setOccupant(player);
        assertEquals(player, territory.getOccupant());
    }

    @Test
    public void isOccupiedBy() {
        territory.setOccupant(player);
        assertTrue(territory.isOccupiedBy(player));
    }

    @Test
    public void isOccupiedByDifferentPlayer() {
        territory.setOccupant(player);
        assertFalse(territory.isOccupiedBy(player2));
    }

    @Test
    public void removeOccupant() {
        territory.setOccupant(player);
        territory.removeOccupant();
        assertEquals(null, territory.getOccupant());
    }

}