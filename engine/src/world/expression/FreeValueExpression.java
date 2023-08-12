package world.expression;

public class FreeValueExpression extends AbstractExpression {
    private final Object value;

    public FreeValueExpression(Object value) {
        super(ExpressionType.FREE_VALUE);
        this.value = value;
    }


    @Override
    public Object getValue() {
        return value;
    }
}
