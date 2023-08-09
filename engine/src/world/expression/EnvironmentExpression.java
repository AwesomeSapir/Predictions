package world.expression;

import world.Context;
import world.instance.property.PropertyInstance;

public class EnvironmentExpression extends AbstractExpression{
    private final Context context;
    private final String envVarName;
    public EnvironmentExpression(Context context, String envVarName) throws IllegalArgumentException{
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.context = context;
        this.envVarName = envVarName;
    }


    @Override
    public Object getValue() throws IllegalArgumentException {
        PropertyInstance envVar = context.getEnvironmentPropertyInstance(envVarName);
        return envVar.getValue();
    }
}
