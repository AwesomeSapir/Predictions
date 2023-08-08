package world.definition.property;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();
    Object generateValue();

    boolean isNumeric();
}
