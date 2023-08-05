package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.Entity;
import world.property.Property;
import world.rule.action.ActionCalc;

public class ActionMultiply extends ActionCalc {

    public ActionMultiply(PRDAction prdObject) {
        super(prdObject);
        arg1 = Double.parseDouble(prdObject.getPRDMultiply().getArg1());
        arg2 = Double.parseDouble(prdObject.getPRDMultiply().getArg2());
    }

    @Override
    public void execute(Entity entity) {
        Property property = entity.getPropertyMap().get(resultPropertyName);
        double newValue = arg1 * arg2;
        if(property.getRange().isInRange(newValue)){
            property.setValue(newValue);
        }
    }
}
