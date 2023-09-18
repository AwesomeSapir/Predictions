package engine.simulation.world.expression;

import engine.simulation.world.definition.property.PropertyType;
import engine.simulation.world.instance.entity.EntityInstance;

public class FreeValueExpression extends AbstractExpression {
    private final Object value;
    private final PropertyType type;

    public FreeValueExpression(Object value, PropertyType type) {
        super(ExpressionType.FREE_VALUE);
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue(EntityInstance entityInstance) {
        return value;
    }

    @Override
    public PropertyType getValueType() {
        return type;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
