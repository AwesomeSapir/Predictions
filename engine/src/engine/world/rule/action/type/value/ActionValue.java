package engine.world.rule.action.type.value;

import engine.world.definition.entity.EntityDefinition;
import engine.world.expression.Expression;
import engine.world.rule.action.Action;
import engine.world.rule.action.ActionType;

public abstract class ActionValue extends Action {

    protected final String propertyName;
    protected final Expression value;

    public ActionValue(ActionType type, EntityDefinition entity, String propertyName, Expression value) {
        super(type, entity);
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
