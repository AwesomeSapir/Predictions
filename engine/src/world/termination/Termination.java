package world.termination;

import engine.prd.PRDBySecond;
import engine.prd.PRDByTicks;
import engine.prd.PRDTermination;

import java.util.ArrayList;
import java.util.List;

public class Termination {
    protected List<TerminationCondition> terminationConditions = new ArrayList<>();

    public Termination(PRDTermination prdObject) {
        for (Object terminationCondition : prdObject.getPRDByTicksOrPRDBySecond()) {
            if (terminationCondition.getClass() == PRDByTicks.class){
                terminationConditions.add(new ByTicks((PRDByTicks) terminationCondition));
            } else if (terminationCondition.getClass() == PRDBySecond.class) {
                terminationConditions.add(new BySecond((PRDBySecond) terminationCondition));
            }
        }
    }
}
