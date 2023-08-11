package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionDivide extends ActionCalc {

    private final double val1 = (double)arg1.getValue();
    private final double val2 = (double)arg2.getValue();

    public ActionDivide(PRDAction prdObject, Context context) throws Exception {
        super(prdObject, context);
        if (val2 == 0){
            throw new IllegalArgumentException("Dividing by 0 is not a valid action.");
        }
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) throws Exception{
            double result = val1 / val2;
            entityInstance.getPropertyByName(resultPropertyName).setValue(result);
    }
}
