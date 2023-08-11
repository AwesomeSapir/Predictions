package world.rule.action.type.condition.single;

import engine.prd.PRDCondition;
import engine.prd.PRDProperty;
import world.Context;
import world.definition.property.PropertyType;
import world.instance.entity.EntityInstance;
import world.rule.action.type.condition.Condition;
import world.rule.action.type.condition.Operator;

public abstract class SingleCondition<T> implements Condition {

    protected Operator operator;
    protected String propertyName;
    protected T value;

    public SingleCondition(PRDCondition prdObject) {
        operator = Operator.fromDRP(prdObject.getOperator());
        propertyName = prdObject.getProperty();
    }

    public abstract boolean evaluate(Context context);

    public static SingleCondition<?> createConditionByType(PRDCondition condition, PropertyType type){
        switch (type){
            case DECIMAL:
                return new SingleIntegerCondition(condition);
                break;
            case BOOLEAN:
                return new SingleBooleanCondition(condition);
                break;
            case FLOAT:
                return new SingleDoubleCondition(condition);
                break;
            case STRING:
                return new SingleStringCondition(condition);
                break;
        }
    }
}
