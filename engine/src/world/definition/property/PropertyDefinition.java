package world.definition.property;

import com.sun.istack.internal.Nullable;
import world.type.Range;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();
    Object generateValue();
    boolean isRandomInit();
    boolean isNumeric();
    @Nullable Range getRange();
}
