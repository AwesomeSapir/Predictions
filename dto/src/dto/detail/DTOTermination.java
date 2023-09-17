package dto.detail;

import java.util.*;

public class DTOTermination extends DTOObject{

    private final Map<String, DTOTerminationCondition<?>> conditions = new HashMap<>();

    public DTOTermination(Collection<DTOTerminationCondition<?>> conditions) {
        super("");
        for (DTOTerminationCondition<?> condition : conditions){
            this.conditions.put(condition.getName(), condition);
        }
    }

    public Collection<DTOTerminationCondition<?>> getConditions() {
        return conditions.values();
    }

    public DTOTerminationCondition<?> getCondition(String type){
        return conditions.get(type);
    }
}
