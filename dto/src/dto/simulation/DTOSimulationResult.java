package dto.simulation;

import dto.detail.DTOTerminationCondition;

public class DTOSimulationResult {

    private final DTOTerminationCondition<?> terminationReason;
    private final int id;

    public DTOSimulationResult(int id, DTOTerminationCondition<?> terminationCondition) {
        this.terminationReason = terminationCondition;
        this.id = id;
    }

    public DTOTerminationCondition<?> getTerminationReason() {
        return terminationReason;
    }

    public int getId() {
        return id;
    }
}
