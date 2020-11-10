package io.github.rsk3110.riskgame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HelpCommandTest {

    private HelpCommand h = new HelpCommand();
    private Player player = null;
    private List<String> args = new ArrayList<String>();

    @Test
    public void execute() {
        assertFalse(h.execute(player));
    }

    @Test
    public void testExecute() {
        assertFalse(h.execute(player, args));
    }
}