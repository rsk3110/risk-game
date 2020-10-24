package io.github.rsk3110.riskgame;

import java.util.List;

public interface Command {
    public void execute();
    public void execute(List<String> args);
}
