package engine.world.rule.action.type.value;

import engine.world.definition.entity.EntityDefinition;
import engine.world.expression.Expression;
import engine.world.Context;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.property.PropertyInstance;
import engine.world.rule.action.ActionType;

public class ActionSet extends ActionValue {

    public ActionSet(ActionType type, EntityDefinition entity, String propertyName, Expression value) {
        super(type, entity, propertyName, value);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        propertyInstance.setValue(value.getValue(entityInstance));
    }
}
