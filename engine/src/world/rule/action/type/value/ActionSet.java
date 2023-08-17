package world.rule.action.type.value;

import world.Context;
import world.expression.Expression;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.ActionType;

public class ActionSet extends ActionValue {

    public ActionSet(ActionType type, String entityName, String propertyName, Expression value) {
        super(type, entityName, propertyName, value);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        propertyInstance.setValue(value.getValue(entityInstance));
    }
}
