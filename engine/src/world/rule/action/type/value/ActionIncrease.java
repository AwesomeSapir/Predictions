package world.rule.action.type.value;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionIncrease extends ActionValue {

    public ActionIncrease(PRDAction prdObject, Context context) {
        super(prdObject, context);
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        double by = (double) value.getValue();
        PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
        double result = (double) propertyInstance.getValue() + by;
        AbstractNumericPropertyDefinition<?> propertyDefinition = (AbstractNumericPropertyDefinition<?>) propertyInstance.getPropertyDefinition();

        if(Validator.validate(Double.toString(result)).isInRange(propertyDefinition.getRange()).isValid()){
            propertyInstance.setValue(result);
        }
    }
}
