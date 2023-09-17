package engine.simulation.world.rule.action.type.value;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.expression.Expression;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.Context;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;

public class ActionIncrease extends ActionValue {

    public ActionIncrease(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, String propertyName, Expression value) {
        super(ActionType.increase, primaryEntity, secondaryEntity, propertyName, value);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double by = Double.parseDouble(value.getValue(entityInstance).toString());
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        double propertyValue = Double.parseDouble(propertyInstance.getValue().toString());
        double result = propertyValue + by;

        propertyInstance.setValue(result);
    }
}
