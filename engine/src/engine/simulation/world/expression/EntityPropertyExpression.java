package engine.simulation.world.expression;

import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.definition.property.PropertyType;
import engine.simulation.world.instance.entity.EntityInstance;

public class EntityPropertyExpression extends AbstractExpression{

    private final PropertyDefinition property;

    public EntityPropertyExpression(PropertyDefinition property){
        super(ExpressionType.ENTITY_PROPERTY);
        this.property = property;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return entityInstance.getPropertyByName(property.getName()).getValue();
    }

    @Override
    public String toString() {
        return property.getName();
    }

    @Override
    public PropertyType getValueType() {
        return property.getType();
    }
}
