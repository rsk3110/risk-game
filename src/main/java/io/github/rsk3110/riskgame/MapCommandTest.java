package io.github.rsk3110.riskgame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MapCommandTest {

    MapCommand m = new MapCommand();
    private World world;
    private Player player = new Player(world, "Player", 2);
    List<String> args = new ArrayList<String>();

    @Test
    public void testExecute() {
        assertFalse(m.execute(player, args));
    }
}