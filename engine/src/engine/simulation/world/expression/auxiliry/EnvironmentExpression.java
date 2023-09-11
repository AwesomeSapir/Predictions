package engine.simulation.world.expression.auxiliry;

import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.property.PropertyInstance;

public class EnvironmentExpression extends AbstractExpression {
    private final PropertyInstance envPropertyInstance;
    public EnvironmentExpression(PropertyInstance propertyInstance){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.envPropertyInstance = propertyInstance;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) throws IllegalArgumentException {
        return envPropertyInstance.getValue();
    }

    @Override
    public String toString() {
        return "environment(" + envPropertyInstance.getPropertyDefinition().getName() + ")";
    }
}
