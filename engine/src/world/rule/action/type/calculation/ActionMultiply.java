package world.rule.action.type.calculation;

import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.expression.Expression;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.ActionType;

public class ActionMultiply extends ActionCalc {

    public ActionMultiply(ActionType type, String entityName, String resultPropertyName, Expression arg1, Expression arg2) {
        super(type, entityName, resultPropertyName, arg1, arg2);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double val1 = (double) arg1.getValue();
        double val2 = (double) arg2.getValue();
        double result = val1 * val2;
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(resultPropertyName);
        AbstractNumericPropertyDefinition<?> numericPropertyDefinition = (AbstractNumericPropertyDefinition<?>) propertyInstance.getPropertyDefinition();
        if (Validator.validate(Double.toString(result)).isInRange(numericPropertyDefinition.getRange()).isValid()) {
            entityInstance.getPropertyByName(resultPropertyName).setValue(result);
        }
    }
}
