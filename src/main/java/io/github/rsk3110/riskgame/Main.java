package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.controller.RiskController;
import io.github.rsk3110.riskgame.controller.world.loader.WorldFileLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Main {
    public static void main(final String[] args) {
        final Path workingDir = Paths.get("").toAbsolutePath();
        final RiskController rc = new RiskController(new WorldFileLoader(workingDir.resolve("worlds")));
        rc.createNewGame("default");
    }
}
