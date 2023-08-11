package world.rule.action.type.value;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.definition.property.AbstractNumericPropertyDefinition;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

public class ActionSet extends ActionValue {

    public ActionSet(PRDAction prdObject, Context context) {
        super(prdObject, context);
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
