package dto.simulation;

import dto.detail.DTOEntity;
import dto.detail.DTOGrid;
import dto.detail.DTORule;
import dto.detail.DTOTermination;

import java.util.Collection;

public class DTOSimulationDetails {

    private final Collection<DTOEntity> entities;
    private final Collection<DTORule> rules;
    private final DTOTermination termination;
    private final DTOGrid grid;

    public DTOSimulationDetails(Collection<DTOEntity> entities, Collection<DTORule> rules, DTOTermination termination, DTOGrid grid) {
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
        this.grid = grid;
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

    public DTOGrid getGrid() {
        return grid;
    }
}
