package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.ValueType;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;
import exception.runtime.IllegalActionException;

public class EvaluateExpression extends AbstractExpression {

    protected final PropertyDefinition propertyDefinition;
    protected final EntityDefinition entityDefinition;

    public EvaluateExpression(EntityDefinition entityDefinition, PropertyDefinition propertyDefinition) {
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.entityDefinition = entityDefinition;
        this.propertyDefinition = propertyDefinition;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) throws IllegalActionException {
        return entityInstance.getPropertyByName(propertyDefinition.getName()).getValue();
    }

    @Override
    public Object getValue(EntityInstance... entityInstances) throws IllegalActionException {
        for (EntityInstance entityInstance : entityInstances){
            if(entityDefinition.equals(entityInstance.getEntityDefinition())){
                return getValue(entityInstance);
            }
        }
        throw new IllegalActionException("Entity instances don't match defined definition");
    }

    @Override
    public ValueType getValueType() {
        return propertyDefinition.getType();
    }

    @Override
    public String toString() {
        return "evaluate(" + entityDefinition.getName() + "." + propertyDefinition.getName() + ")";
    }
}
