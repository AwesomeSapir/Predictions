package engine.simulation.world.expression;

import engine.simulation.world.ValueType;
import engine.simulation.world.instance.entity.EntityInstance;
import exception.runtime.IllegalActionException;

import java.io.Serializable;

public abstract class AbstractExpression implements Expression, Serializable {
    private final ExpressionType type;

    public AbstractExpression(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }
    @Override
    public abstract Object getValue(EntityInstance entityInstance) throws IllegalActionException;
    @Override
    public abstract Object getValue(EntityInstance... entityInstances) throws IllegalActionException;

    @Override
    public abstract ValueType getValueType();
}
