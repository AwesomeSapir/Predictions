package engine.world.expression;

import engine.world.instance.entity.EntityInstance;

import java.util.Random;

public class RandomExpression extends AbstractExpression{
    private final int arg;

    private final Random random = new Random();
    public RandomExpression(int arg){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.arg = arg;
    }

    @Override
    public Object getValue(EntityInstance entityInstance){
        return random.nextInt(arg + 1);
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.AUXILIARY_FUNCTION;
    }

    @Override
    public String toString() {
        return "random(" + arg + ")";
    }
}
