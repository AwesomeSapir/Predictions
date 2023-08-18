package engine.world.rule.action.type.value;

import engine.world.expression.Expression;
import engine.world.Context;
import engine.world.instance.entity.EntityInstance;
import engine.world.instance.property.PropertyInstance;
import engine.world.rule.action.ActionType;

public class ActionIncrease extends ActionValue {

    public ActionIncrease(ActionType type, String entityName, String propertyName, Expression value) {
        super(type, entityName, propertyName, value);
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
