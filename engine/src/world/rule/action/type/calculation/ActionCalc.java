package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import world.Context;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.rule.action.Action;

public abstract class ActionCalc extends Action {

    protected String resultPropertyName;
    protected Expression arg1;
    protected Expression arg2;

    public ActionCalc(PRDAction prdObject, Context context) {
        super(prdObject);
        arg1 = ExpressionDecoder.decodeExpression(prdObject.getPRDMultiply().getArg1(), prdObject.getResultProp(), context);
        arg2 = ExpressionDecoder.decodeExpression(prdObject.getPRDMultiply().getArg2(), prdObject.getResultProp(), context);
    }
}
