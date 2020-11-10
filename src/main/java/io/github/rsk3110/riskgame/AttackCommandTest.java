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
    List<Territory> territories;

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
    }

    @Test
    public void testExecute() {
    }
}