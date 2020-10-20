package io.github.rsk3110.riskgame.controller.fsm;

        import io.github.rsk3110.riskgame.controller.RiskController;
        import org.squirrelframework.foundation.fsm.StateMachineBuilder;
        import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
        import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class RiskStateMachine extends AbstractStateMachine<RiskStateMachine, RiskState, RiskEvent, RiskController> {
    private static final RiskStateMachine INSTANCE;

    public static RiskStateMachine getInstance() {
        return INSTANCE;
    }

    static {
        final StateMachineBuilder<RiskStateMachine, RiskState, RiskEvent, RiskController> builder =
                StateMachineBuilderFactory.create(RiskStateMachine.class, RiskState.class, RiskEvent.class, RiskController.class);
        INSTANCE = builder.newStateMachine(RiskState.INITIAL_LOAD);

    }
}
