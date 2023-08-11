package world.rule.action.type.value;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.expression.EntityPropertyExpression;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionIncrease extends ActionValue {

    public ActionIncrease(PRDAction prdObject, Context context)throws Exception {
        super(prdObject, context);
        if(!context.getPrimaryEntityDefinition().getProperties().get(propertyName).isNumeric())
            throw new IllegalArgumentException("Property '" + propertyName + "' referenced to property in Increase is not of a numeric type.");

        if(value instanceof EntityPropertyExpression){
            if(!context.getPrimaryEntityDefinition().getProperties().get(value.toString()).isNumeric())
                throw new IllegalArgumentException("Property '" + value + "' referenced to value in Increase is not of a numeric type.");
        }
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double by = (double) value.getValue();
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        double result = (double) propertyInstance.getValue() + by;

        if(!Validator.validate(Double.toString(result)).isWholeInteger(result).isValid() && type.toString().equalsIgnoreCase("DECIMAL")){
            throw new IllegalArgumentException("Property " + propertyName + " must get only integer values.");
        }

            propertyInstance.setValue(result);
    }
}
