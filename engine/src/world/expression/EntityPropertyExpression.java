package world.expression;

import world.instance.entity.EntityInstance;

public class EntityPropertyExpression extends AbstractExpression{

    public EntityPropertyExpression(String propertyName){
        super(ExpressionType.ENTITY_PROPERTY);
        value = propertyName;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return entityInstance.getPropertyByName(value.toString()).getValue();
    }
}
