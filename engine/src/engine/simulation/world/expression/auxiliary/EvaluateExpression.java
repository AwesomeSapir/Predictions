package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.definition.property.PropertyType;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;

public class EvaluateExpression extends AbstractExpression {

    protected final PropertyDefinition propertyDefinition;
    protected final EntityDefinition entityDefinition;

    public EvaluateExpression(EntityDefinition entityDefinition, PropertyDefinition propertyDefinition) {
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.entityDefinition = entityDefinition;
        this.propertyDefinition = propertyDefinition;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        if(entityInstance.getEntityDefinition().equals(entityDefinition)){
            return entityInstance.getPropertyByName(propertyDefinition.getName()).getValue();
        }
        return null;
    }

    @Override
    public PropertyType getValueType() {
        return propertyDefinition.getType();
    }

    @Override
    public String toString() {
        return "evaluate(" + entityDefinition.getName() + "." + propertyDefinition.getName() + ")";
    }
}
