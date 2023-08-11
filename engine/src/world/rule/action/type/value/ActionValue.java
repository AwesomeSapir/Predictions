package world.rule.action.type.value;

import engine.prd.PRDAction;
import world.Context;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.rule.action.Action;

public abstract class ActionValue extends Action {

    protected String propertyName;
    protected Expression value;

    public ActionValue(PRDAction prdObject, Context context) {
        super(prdObject);
        propertyName = prdObject.getProperty();
        //TODO when removing prd, send getBy or getValue depending on if set or increase
        value = ExpressionDecoder.decodeExpression(prdObject.getBy(), propertyName, context);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Expression getValue() {
        return value;
    }
}
