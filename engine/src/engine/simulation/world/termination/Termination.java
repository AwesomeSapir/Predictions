package engine.simulation.world.termination;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.*;

public class Termination implements Serializable {

    private final Map<Type, TerminationCondition> terminationConditions = new HashMap<>();
    private TerminationCondition reason;

    public Termination(TerminationCondition... terminationConditions) {
        for (TerminationCondition terminationCondition : terminationConditions){
            if(terminationCondition != null) {
                this.terminationConditions.put(terminationCondition.getType(), terminationCondition);
            }
        }
    }

    public boolean isMet(){
        for (TerminationCondition terminationCondition : terminationConditions.values()){
            if(terminationCondition.isMet()){
                reason = terminationCondition;
                return true;
            }
        }
        return false;
    }

    public Collection<TerminationCondition> getTerminationConditions() {
        return terminationConditions.values();
    }

    public @Nullable TerminationCondition getTerminationCondition(Type type){
        return terminationConditions.get(type);
    }

    public void addTerminationCondition(TerminationCondition condition){
        terminationConditions.put(condition.getType(), condition);
    }

    public TerminationCondition getReason() {
        return reason;
    }

    public enum Type {
        SECONDS, TICKS, USER, ERROR
    }
}
