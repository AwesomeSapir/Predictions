package exception.invalid.calculation.multiply;
import engine.world.validation.exception.invalid.calculation.InvalidCalculationException;
public class InvalidMultiplyException extends InvalidCalculationException{
    public InvalidMultiplyException(String message) {
        super(message);
    }
}
