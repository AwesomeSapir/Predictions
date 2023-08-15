package dto.simulation;

import dto.detail.DTOEntity;
import dto.detail.DTORule;
import dto.detail.DTOTermination;

import java.util.Collection;

public class DTOSimulationDetails {

    private final Collection<DTOEntity> entities;
    private final Collection<DTORule> rules;
    private final DTOTermination termination;

    public DTOSimulationDetails(Collection<DTOEntity> entities, Collection<DTORule> rules, DTOTermination termination) {
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
    }

    public Collection<DTOEntity> getEntities() {
        return entities;
    }

    public Collection<DTORule> getRules() {
        return rules;
    }

    public DTOTermination getTermination() {
        return termination;
    }
}
