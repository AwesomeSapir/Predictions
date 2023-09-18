package engine.simulation.world.rule.action.type.condition;

import engine.simulation.world.Context;
import engine.simulation.world.instance.entity.EntityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiCondition implements Condition, Serializable {

    protected final List<Condition> subConditions = new ArrayList<>();
    protected final Logical logical;

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

    @Override
    public boolean evaluate(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context) {
        Boolean overallResult = null;
        for (Condition condition : subConditions){
            boolean result = condition.evaluate(primaryEntity, secondaryEntity, context);
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String logicalStr = "";
        switch (logical){
            case and:
                logicalStr = " && ";
                break;
            case or:
                logicalStr = " || ";
                break;
        }
        int index = 0;
        for (Condition condition : subConditions){
            result.append(condition.toString()).append((index < subConditions.size() - 1) ? logicalStr : "");
            index++;
        }

        return "(" + result + ")";
    }
}
