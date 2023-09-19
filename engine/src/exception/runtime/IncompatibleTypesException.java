package exception.runtime;

public class IncompatibleTypesException extends SimulationRuntimeException{

    public IncompatibleTypesException(String message) {
        super(message, "Incompatible Types");
    }
}
