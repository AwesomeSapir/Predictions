package engine.world.expression;

import engine.world.instance.entity.EntityInstance;
import engine.world.instance.property.PropertyInstance;

public class EnvironmentExpression extends AbstractExpression{
    private final PropertyInstance envPropertyInstance;
    public EnvironmentExpression(PropertyInstance propertyInstance){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.envPropertyInstance = propertyInstance;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) throws IllegalArgumentException {
        return envPropertyInstance.getValue();
    }
}
