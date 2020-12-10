package io.github.rsk3110.riskgame;

import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test continent class
 *
 * @author Tooba Sheikh
 **/
public class ContinentTest {

    private Continent c = new Continent("A", null, 2);
    private Territory t = new Territory(1);
    private Territory t2 = new Territory(2);

    private Set<Territory> territories = new HashSet<Territory>();;

    @Test
    public void getName() {
        assertEquals("A",c.getName());
    }

    @Test
    public void getColor() {
        assertEquals(null,c.getColor());
    }

    @Test
    public void getBonusArmies() {
        assertEquals(2,c.getBonusArmies());
    }

    @Test
    public void getTerritories() {
        territories.add(t);
        territories.add(t2);
        c.addTerritory(t);
        c.addTerritory(t2);
        assertEquals(territories, c.getTerritories());
    }

    @Test
    public void addTerritory() {
        c.addTerritory(t);
        assertTrue(c.getTerritories().contains(t));
    }
}