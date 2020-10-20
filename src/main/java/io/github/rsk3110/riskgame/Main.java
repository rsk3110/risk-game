package io.github.rsk3110.riskgame;

import io.github.rsk3110.riskgame.controller.RiskController;
import io.github.rsk3110.riskgame.controller.world.loader.WorldFileLoader;
import io.github.rsk3110.riskgame.view.cli.CLIView;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Main {
    public static void main(final String[] args) {
        final Path workingDir = Paths.get("").toAbsolutePath();
        new RiskController(
                CLIView::new,
                new WorldFileLoader(workingDir.resolve("worlds"))
        ).run();
    }
}
