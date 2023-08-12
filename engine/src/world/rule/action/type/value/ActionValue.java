package world.rule.action.type.value;

import world.expression.Expression;
import world.rule.action.Action;
import world.rule.action.ActionType;

public abstract class ActionValue extends Action {

    protected String propertyName;
    protected Expression value;

    public ActionValue(ActionType type, String entityName, String propertyName, Expression value) {
        super(type, entityName);
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Expression getValue() {
        return value;
    }
}
