package dto.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DTOSimulationHistogram {

    private final Map<Object, Integer> valueToCount = new HashMap<>();

    public DTOSimulationHistogram(Collection<Object> values) {
        for(Object value : values) {
            valueToCount.put(value, valueToCount.getOrDefault(value, 0) + 1);
        }
    }

    public Map<Object, Integer> getValueToCount() {
        return valueToCount;
    }
}
