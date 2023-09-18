package engine.simulation.world.expression;

import engine.simulation.world.ValueType;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.instance.entity.EntityInstance;

public class EntityPropertyExpression extends AbstractExpression{

    private final PropertyDefinition property;

    public EntityPropertyExpression(PropertyDefinition property){
        super(ExpressionType.ENTITY_PROPERTY);
        this.property = property;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return entityInstance.getPropertyIfExists(property.getName()).getValue();
    }

    @Override
    public Object getValue(EntityInstance... entityInstances) { //TODO ask aviad
        for (EntityInstance entityInstance : entityInstances){
            try {
                return getValue(entityInstance);
            } catch (NullPointerException e){

            }
        }
        throw new RuntimeException("Entity instances don't match defined definition");
    }

    @Override
    public String toString() {
        return property.getName();
    }

    @Override
    public ValueType getValueType() {
        return property.getType();
    }
}
