package world.rule.action.type.condition;

import engine.prd.PRDCondition;
import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.type.condition.single.SingleCondition;

import java.util.ArrayList;
import java.util.List;

public class MultiCondition implements Condition {

    protected List<Condition> subConditions = new ArrayList<>();
    protected Logical logical = Logical.and;

    public MultiCondition(PRDCondition prdObject) {
        Singularity singularity = Singularity.valueOf(prdObject.getSingularity());
        if (singularity == Singularity.multiple){
            logical = Logical.valueOf(prdObject.getLogical());
            for (PRDCondition prdCondition : prdObject.getPRDCondition()){
                switch (Singularity.valueOf(prdCondition.getSingularity())) {
                    case single:
                        subConditions.add(new SingleCondition(prdObject));
                        break;
                    case multiple:
                        subConditions.add(new MultiCondition(prdCondition));
                        break;
                }
            }
        } else {
            subConditions.add(new SingleCondition(prdObject));
        }
    }

    @Override
    public boolean evaluate(Context context) {
        Boolean overallResult = null;
        for (Condition condition : subConditions){
            boolean result = condition.evaluate(entity);
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
