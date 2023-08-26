package engine.world.expression;

import engine.world.instance.entity.EntityInstance;

public class EntityPropertyExpression extends AbstractExpression{

    private final String propertyName;

    public EntityPropertyExpression(String propertyName){
        super(ExpressionType.ENTITY_PROPERTY);
        this.propertyName = propertyName;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return entityInstance.getPropertyByName(propertyName).getValue();
    }

    @Override
    public String toString() {
        return propertyName;
    }
}
