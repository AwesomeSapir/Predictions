package world.rule.action;

import engine.prd.PRDAction;

public abstract class ActionCalc extends Action{

    protected String resultPropertyName;
    protected double arg1;
    protected double arg2;

    public ActionCalc(PRDAction prdObject) {
        super(prdObject);
    }
}
