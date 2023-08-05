package world.rule.action.type.condition;

public enum Operator {
    neq("!="), eq("="), bt("bt"), lt("lt");

    private String operator;
    private Operator(String s) {
        operator = s;
    }

    public String getOperator() {
        return operator;
    }
}
