package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.ActionCalc;

public class ActionMultiply extends ActionCalc {

    public ActionMultiply(PRDAction prdObject) {
        super(prdObject);
        arg1 = Double.parseDouble(prdObject.getPRDMultiply().getArg1());
        arg2 = Double.parseDouble(prdObject.getPRDMultiply().getArg2());
    }

    @Override
    public void execute(EntityInstance entity) {
        PropertyInstance property = entity.getPropertyByName(resultPropertyName);
        double newValue = arg1 * arg2;
        property.setValue(newValue);
    }
}
