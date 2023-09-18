package engine.simulation.world.definition.property;

import com.sun.istack.internal.Nullable;
import engine.simulation.world.ValueType;
import engine.simulation.world.type.Range;

public interface PropertyDefinition {
    String getName();
    ValueType getType();
    Object generateValue();
    boolean isRandomInit();
    boolean isNumeric();
    @Nullable
    Range getRange();
}
