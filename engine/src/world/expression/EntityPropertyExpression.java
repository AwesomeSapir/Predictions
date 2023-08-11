package world.expression;

public class EntityPropertyExpression extends AbstractExpression{

    public EntityPropertyExpression(String propertyName){
        super(ExpressionType.ENTITY_PROPERTY);
        value = propertyName;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
