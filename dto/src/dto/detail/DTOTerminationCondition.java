package dto.detail;

public class DTOTerminationCondition<T> extends DTOObject {

    private final T condition;

    public DTOTerminationCondition(String name, T condition) {
        super(name);
        this.condition = condition;
    }

    public T getCondition() {
        return condition;
    }
}
