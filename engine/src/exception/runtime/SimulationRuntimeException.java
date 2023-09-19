package exception.runtime;

import exception.EngineException;

public abstract class SimulationRuntimeException extends EngineException {

    private final String secondaryType;
    public SimulationRuntimeException(String message, String secondaryType) {
        super(message, "Simulation Runtime");
        this.secondaryType = secondaryType;
    }

    public String getSecondaryType() {
        return secondaryType;
    }
}
