package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.Context;
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
    public void execute(Context context) {

    }

}
