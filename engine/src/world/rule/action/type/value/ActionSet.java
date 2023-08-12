package world.rule.action.type.value;

import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
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
        double result = (double) value.getValue();
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);

        if(propertyInstance.getPropertyDefinition().isNumeric()){
            AbstractNumericPropertyDefinition<?> numericPropertyDefinition = (AbstractNumericPropertyDefinition<?>) propertyInstance.getPropertyDefinition();
            if(!Validator.validate(Double.toString(result)).isInRange(numericPropertyDefinition.getRange()).isValid()){
                return;
            }
        }

        propertyInstance.setValue(result);
    }
}
