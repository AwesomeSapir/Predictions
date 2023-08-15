package dto.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DTOSimulationHistogram {

    private final String propertyName;
    private final Map<Object, Integer> valueToCount = new HashMap<>();

    public DTOSimulationHistogram(Collection<Object> values, String propertyName) {
        for(Object value : values) {
            valueToCount.put(value, valueToCount.getOrDefault(value, 0) + 1);
        }
        this.propertyName = propertyName;
    }

    public Map<Object, Integer> getValueToCount() {
        return valueToCount;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
