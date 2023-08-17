package world.rule.action.type.value;

import world.Context;
import world.expression.Expression;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.ActionType;

public class ActionDecrease extends ActionValue {

    public ActionDecrease(ActionType type, String entityName, String propertyName, Expression value) {
        super(type, entityName, propertyName, value);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double by = Double.parseDouble(value.getValue(entityInstance).toString());
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        double propertyValue = Double.parseDouble(propertyInstance.getValue().toString());
        double result = propertyValue - by;

        propertyInstance.setValue(result);
    }
}
