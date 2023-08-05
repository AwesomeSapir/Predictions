package world.rule.action.type.condition;

import engine.prd.PRDCondition;

import java.util.List;

public class MultiCondition implements Condition {

    protected List<Condition> subConditions;
    protected Logical logical = Logical.and;

    public MultiCondition(PRDCondition prdObject) {
        Singularity singularity = Singularity.valueOf(prdObject.getPRDCondition().getSingularity());

    }

    @Override
    public boolean evaluate() {
        Boolean overallResult = null;
        for (Condition condition : subConditions){
            boolean result = condition.evaluate();
            if (overallResult == null){
                overallResult = result;
            }
            switch (logical){
                case and:
                    overallResult = overallResult && result;
                    break;
                case or:
                    overallResult = overallResult || result;
                    break;
            }
        }
        return overallResult;
    }
}
