package exception;

public class SimulationMissingException extends EngineException {

    public SimulationMissingException(String message) {
        super(message, "Simulation Not Loaded");
    }
}
