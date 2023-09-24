package engine.simulation.world.expression.auxiliary;

import engine.simulation.world.Context;
import engine.simulation.world.ValueType;
import engine.simulation.world.expression.AbstractExpression;
import engine.simulation.world.expression.ExpressionType;
import engine.simulation.world.instance.entity.EntityInstance;

import java.util.Random;

public class RandomExpression extends AbstractExpression {
    private final int arg;

    private final Random random = new Random();
    public RandomExpression(int arg){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.arg = arg;
    }

    @Override
    public Object getValue(EntityInstance entityInstance, Context context){
        return random.nextInt(arg + 1);
    }

    @Override
    public Object getValue(Context context, EntityInstance... entityInstances) {
        return random.nextInt(arg + 1);
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.AUXILIARY_FUNCTION;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.FLOAT;
    }

    @Override
    public String toString() {
        return "random(" + arg + ")";
    }
}
