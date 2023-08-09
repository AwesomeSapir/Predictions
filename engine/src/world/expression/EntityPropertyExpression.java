package world.expression;

import world.Context;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;

import java.util.Collection;

public class EntityPropertyExpression extends AbstractExpression{
    private final PropertyInstance propertyInstance;

    public EntityPropertyExpression(PropertyInstance propertyInstance){
        super(ExpressionType.ENTITY_PROPERTY);
        this.propertyInstance = propertyInstance;
    }

    @Override
    public Object getValue() {
        return propertyInstance.getValue();
    }
}
