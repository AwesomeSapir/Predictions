package dto.detail;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DTOEntity extends DTOObject {
    private final int population;
    private final Collection<DTOProperty> properties;

    public DTOEntity(String name, int population, Collection<DTOProperty> properties) {
        super(name);
        this.population = population;
        this.properties = properties;
    }

    public int getPopulation() {
        return population;
    }

    public Collection<DTOProperty> getProperties() {
        return properties;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = new LinkedHashMap<>();
        fieldValues.put("Name", name);
        fieldValues.put("Population", String.valueOf(population));
        fieldValues.put("Properties", properties.toString());
        return fieldValues;
    }
}
