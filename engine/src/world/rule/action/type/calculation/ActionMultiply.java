package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionMultiply extends ActionCalc {

    public ActionMultiply(PRDAction prdObject, Context context) {
        super(prdObject, context);
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
