package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.ActionCalc;

public class ActionDivide extends ActionCalc {

    public ActionDivide(PRDAction prdObject) {
        super(prdObject);
        arg1 = Double.parseDouble(prdObject.getPRDDivide().getArg1());
        arg2 = Double.parseDouble(prdObject.getPRDDivide().getArg2());
    }

    @Override
    public void execute(EntityInstance entity) {
        PropertyInstance property = entity.getPropertyByName(resultPropertyName);
        if(arg2 != 0){
            double newValue = arg1 / arg2;
            property.setValue(newValue);
        }
    }

}
