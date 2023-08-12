package world.rule.action.type.condition;

import world.Context;
import world.expression.Expression;

public class SingleCondition implements Condition {

    protected Operator operator;
    protected String propertyName;
    protected Expression value;

    public SingleCondition(Operator operator, String propertyName, Expression value) {
        this.operator = operator;
        this.propertyName = propertyName;
        this.value = value;
    }

    public boolean evaluate(Context context){
        //TODO
        return false;
    }
}
