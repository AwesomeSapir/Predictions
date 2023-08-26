package engine.world.rule.action.type.calculation;

import engine.world.expression.Expression;
import engine.world.rule.action.Action;
import engine.world.rule.action.ActionType;

public abstract class ActionCalc extends Action {

    protected final String resultPropertyName;
    protected final Expression arg1;
    protected final Expression arg2;

    public ActionCalc(ActionType type, String entityName, String resultPropertyName, Expression arg1, Expression arg2) {
        super(type, entityName);
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
