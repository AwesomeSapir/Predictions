package dto.simulation;

import dto.detail.DTOEntity;

public class DTOEntityPopulation {

    private final int initialPopulation;
    private final int finalPopulation;
    private final DTOEntity entity;

    public DTOEntityPopulation(int initialPopulation, int finalPopulation, DTOEntity entity) {
        this.initialPopulation = initialPopulation;
        this.finalPopulation = finalPopulation;
        this.entity = entity;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public int getFinalPopulation() {
        return finalPopulation;
    }

    public DTOEntity getEntity() {
        return entity;
    }
}
