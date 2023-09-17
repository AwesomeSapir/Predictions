package engine.simulation.world.rule.action.type.value;

import engine.simulation.world.expression.Expression;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

public abstract class ActionValue extends Action {

    protected final String propertyName;
    protected final Expression value;

    public ActionValue(ActionType type, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, String propertyName, Expression value) {
        super(type, primaryEntity, secondaryEntity);
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
