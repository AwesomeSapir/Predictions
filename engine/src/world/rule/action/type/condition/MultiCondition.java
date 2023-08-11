package world.rule.action.type.condition;

import engine.prd.PRDCondition;
import world.Context;

import java.util.ArrayList;
import java.util.List;

public class MultiCondition implements Condition {

    protected List<Condition> subConditions = new ArrayList<>();
    protected Logical logical = Logical.and;

    public MultiCondition(PRDCondition prdObject, Context context) {
        Singularity singularity = Singularity.valueOf(prdObject.getSingularity());
        if (singularity == Singularity.multiple){
            logical = Logical.valueOf(prdObject.getLogical());
            for (PRDCondition prdCondition : prdObject.getPRDCondition()){
                switch (Singularity.valueOf(prdCondition.getSingularity())) {
                    case single:
                        subConditions.add(new SingleCondition(prdObject, context));
                        break;
                    case multiple:
                        subConditions.add(new MultiCondition(prdCondition, context));
                        break;
                }
            }
        } else {
            subConditions.add(new SingleCondition(prdObject, context));
        }
    }

    @Override
    public boolean evaluate(Context context) {
        Boolean overallResult = null;
        for (Condition condition : subConditions){
            boolean result = condition.evaluate(context);
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
