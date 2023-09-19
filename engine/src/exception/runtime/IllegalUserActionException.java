package exception.runtime;

public class IllegalUserActionException extends SimulationRuntimeException{

    public IllegalUserActionException(String message) {
        super(message, "Illegal User Action");
    }
}
