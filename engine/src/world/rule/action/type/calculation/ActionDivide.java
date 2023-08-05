package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.Entity;
import world.property.Property;
import world.rule.action.ActionCalc;

public class ActionDivide extends ActionCalc {

    public ActionDivide(PRDAction prdObject) {
        super(prdObject);
        arg1 = Double.parseDouble(prdObject.getPRDDivide().getArg1());
        arg2 = Double.parseDouble(prdObject.getPRDDivide().getArg2());
    }

    @Override
    public void execute(Entity entity) {
        Property property = entity.getPropertyMap().get(resultPropertyName);
        if(arg2 != 0){
            double newValue = arg1 / arg2;
            if(property.getRange().isInRange(newValue)){
                property.setValue(newValue);
            }
        }
    }

}
