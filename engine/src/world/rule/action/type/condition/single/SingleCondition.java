package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import engine.prd.PRDProperty;
import world.definition.property.PropertyType;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.instance.entity.EntityInstance;
import world.rule.action.type.condition.Condition;
import world.rule.action.type.condition.Operator;

public abstract class SingleCondition implements Condition {

    protected Operator operator;
    protected String propertyName;
    protected Expression value;

    public SingleCondition(PRDCondition prdObject) {
        operator = Operator.fromDRP(prdObject.getOperator());
        propertyName = prdObject.getProperty();
        value = ExpressionDecoder.decodeExpression(prdObject.getValue(), propertyName, context);
    }

    public abstract boolean evaluate(EntityInstance entity);
}
