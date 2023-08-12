package world.rule.action.type.calculation;

import world.expression.Expression;
import world.rule.action.Action;
import world.rule.action.ActionType;

public abstract class ActionCalc extends Action {

    protected String resultPropertyName;
    protected Expression arg1;
    protected Expression arg2;

    public ActionCalc(ActionType type, String entityName, String resultPropertyName, Expression arg1, Expression arg2) {
        super(type, entityName);
        this.resultPropertyName = resultPropertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
