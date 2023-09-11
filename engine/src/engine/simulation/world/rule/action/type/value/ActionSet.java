package engine.simulation.world.rule.action.type.value;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.expression.Expression;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.Context;
import engine.simulation.world.rule.action.ActionType;

public class ActionSet extends ActionValue {

    public ActionSet(EntityDefinition primaryEntity, EntityDefinition secondaryEntity, String propertyName, Expression value) {
        super(ActionType.set, primaryEntity, secondaryEntity, propertyName, value);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        propertyInstance.setValue(value.getValue(entityInstance));
    }
}
