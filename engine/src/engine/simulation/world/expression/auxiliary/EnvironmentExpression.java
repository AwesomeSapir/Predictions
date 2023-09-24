package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.Context;
import engine.simulation.world.ValueType;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;

public class EnvironmentExpression extends AbstractExpression {
    private final PropertyDefinition envPropertyDefintion;

    public EnvironmentExpression(PropertyDefinition propertyDefinition){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.envPropertyDefintion = propertyDefinition;
    }

    @Override
    public Object getValue(EntityInstance entityInstance, Context context) {
        return context.getEnvironmentPropertyInstance(envPropertyDefintion.getName()).getValue();
    }

    @Override
    public Object getValue(Context context, EntityInstance... entityInstances) {
        return context.getEnvironmentPropertyInstance(envPropertyDefintion.getName()).getValue();
    }

    @Override
    public ValueType getValueType() {
        return envPropertyDefintion.getType();
    }

    @Override
    public String toString() {
        return "environment(" + envPropertyDefintion.getName() + ")";
    }
}
