package io.github.rsk3110.riskgame.view.cli;

import io.github.rsk3110.riskgame.controller.RiskController;
import io.github.rsk3110.riskgame.view.RiskView;

public final class CLIView implements RiskView {
    private final RiskController rc;

    public CLIView(final RiskController rc) {
        this.rc = rc;
    }
}
