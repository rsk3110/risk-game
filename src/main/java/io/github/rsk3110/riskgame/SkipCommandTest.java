package io.github.rsk3110.riskgame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SkipCommandTest {

    private SkipCommand s = new SkipCommand();
    private Player player = null;
    private List<String> args = new ArrayList<String>();

    @Test
    public void execute() {
        assertTrue(s.execute(player));
    }

    @Test
    public void testExecute() {
        assertFalse(s.execute(player, args));
    }
}