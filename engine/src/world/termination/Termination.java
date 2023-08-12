package world.termination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Termination {
    protected List<TerminationCondition> terminationConditions = new ArrayList<>();

    public Termination(Collection<TerminationCondition> terminationConditions) {
        this.terminationConditions.addAll(terminationConditions);
    }
}
