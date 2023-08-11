package world.rule.action.type.condition;

import engine.prd.PRDCondition;
import world.Context;
import world.expression.Expression;
import world.expression.ExpressionDecoder;

public class SingleCondition implements Condition {

    protected Operator operator;
    protected String propertyName;
    protected Expression value;

    public SingleCondition(PRDCondition prdObject, Context context) {
        operator = Operator.fromDRP(prdObject.getOperator());
        propertyName = prdObject.getProperty();
        value = ExpressionDecoder.decodeExpression(prdObject.getValue(), propertyName, context);
    }

    public boolean evaluate(Context context){
        //TODO
    }
}
