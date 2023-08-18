package engine.world.rule.action.type.value;

import engine.world.expression.Expression;
import engine.world.rule.action.Action;
import engine.world.rule.action.ActionType;

public abstract class ActionValue extends Action {

    protected final String propertyName;
    protected final Expression value;

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
