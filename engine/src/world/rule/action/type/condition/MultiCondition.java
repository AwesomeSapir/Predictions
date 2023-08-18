package world.rule.action.type.condition;

import world.Context;
import world.instance.entity.EntityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiCondition implements Condition, Serializable {

    protected List<Condition> subConditions = new ArrayList<>();
    protected Logical logical;

    public MultiCondition(Logical logical, Collection<Condition> subConditions) {
        this.logical = logical;
        this.subConditions.addAll(subConditions);
    }

    @Override
    public boolean evaluate(EntityInstance entityInstance, Context context) {
        Boolean overallResult = null;
        for (Condition condition : subConditions){
            boolean result = condition.evaluate(entityInstance, context);
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
