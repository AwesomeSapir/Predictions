package engine.world.rule.action.type.condition;

public enum Operator {
    neq("!="), eq("="), bt("bt"), lt("lt");

    private final String operator;
    Operator(String s) {
        operator = s;
    }
    public String getOperator() {
        return operator;
    }

    public static Operator fromDRP(String value){
        for (Operator operator : Operator.values()) {
            if (operator.getOperator().equals(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown XML operator value: " + value);
    }
}
