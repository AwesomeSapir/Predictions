package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;

public class TicksExpression extends AbstractExpression {
    protected final EntityDefinition entityDefinition;
    protected final PropertyDefinition propertyDefinition;

    public TicksExpression(EntityDefinition entityDefinition, PropertyDefinition propertyDefinition) {
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.entityDefinition = entityDefinition;
        this.propertyDefinition = propertyDefinition;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        if(entityInstance.getEntityDefinition().equals(entityDefinition)){
            return entityInstance.getPropertyByName(propertyDefinition.getName()).getTicksOfSameValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "ticks(" + entityDefinition.getName() + "." + propertyDefinition.getName() + ")";
    }
}

