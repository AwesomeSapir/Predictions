package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionMultiply extends ActionCalc {

    private final double val1 = (double)arg1.getValue();
    private final double val2 = (double)arg2.getValue();
    private final double result = val1 * val2;

    public ActionMultiply(PRDAction prdObject, Context context) throws Exception {
        super(prdObject, context);
        if(!Validator.validate(Double.toString(result)).isWholeInteger(result).isValid() && type.toString().equalsIgnoreCase("DECIMAL")){
            throw new IllegalArgumentException("Property " + resultPropertyName + " must get only integer values.");
        }
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        entityInstance.getPropertyByName(resultPropertyName).setValue(result);
    }
}
