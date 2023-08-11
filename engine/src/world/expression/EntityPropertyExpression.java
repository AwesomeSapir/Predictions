package world.expression;

public class EntityPropertyExpression extends AbstractExpression{
    private final String propertyName;

    public EntityPropertyExpression(String propertyName){
        super(ExpressionType.ENTITY_PROPERTY);
        this.propertyName = propertyName;
    }

    @Override
    public Object getValue() {
        return propertyName;
    }
}
