package io.github.rsk3110.riskgame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuitCommandTest {

    private QuitCommand q = new QuitCommand();
    private Player player = null;
    private List<String> args = new ArrayList<String>();

    @Test
    public void testExecute() { assertFalse(q.execute(player, args)); }

    @Test
    public void execute() {
        assertFalse(q.execute(player));
    }

}