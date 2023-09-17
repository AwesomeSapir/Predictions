package engine.simulation.world.rule.action.type.calculation;

import engine.simulation.world.expression.Expression;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

public abstract class ActionCalc extends Action {

    protected final String resultPropertyName;
    protected final Expression arg1;
    protected final Expression arg2;

    public ActionCalc(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, String resultPropertyName, Expression arg1, Expression arg2) {
        super(ActionType.calculation, primaryEntity, secondaryEntity);
        this.resultPropertyName = resultPropertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getResultPropertyName() {
        return resultPropertyName;
    }

    public Expression getArg1() {
        return arg1;
    }

    public Expression getArg2() {
        return arg2;
    }
}
