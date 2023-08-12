package world.expression;

import world.instance.property.PropertyInstance;

public class EnvironmentExpression extends AbstractExpression{
    private final PropertyInstance envPropertyInstance;
    public EnvironmentExpression(PropertyInstance propertyInstance){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.envPropertyInstance = propertyInstance;
    }

    @Override
    public Object getValue() throws IllegalArgumentException {
        return envPropertyInstance.getValue();
    }
}
