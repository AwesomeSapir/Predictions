package exception.runtime;

public class IllegalActionException extends SimulationRuntimeException {

    public IllegalActionException(String message) {
        super(message, "Illegal Action");
    }
}
