package world.rule.action.type.value;

import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
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
        double by = (double) value.getValue();
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        double result = (double) propertyInstance.getValue() - by;
        AbstractNumericPropertyDefinition<?> propertyDefinition = (AbstractNumericPropertyDefinition<?>) propertyInstance.getPropertyDefinition();

        if(Validator.validate(Double.toString(result)).isInRange(propertyDefinition.getRange()).isValid()){
            propertyInstance.setValue(result);
        }
    }
}
