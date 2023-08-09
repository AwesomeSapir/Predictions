package world.expression;

import world.Context;
import world.instance.property.PropertyInstance;

import java.util.Random;

public class RandomExpression extends AbstractExpression{
    private final int arg;

    private final Random random = new Random();
    public RandomExpression(int arg){
        super(ExpressionType.AUXILIARY_FUNCTION);
        this.arg = arg;
    }

    @Override
    public Object getValue(){
        return random.nextInt(arg + 1);
    }

    @Override
    public ExpressionType getType() {
        return null;
    }
}
