package ui.component.custom.input.simulation;

import dto.detail.DTOEntity;
import ui.component.custom.input.generic.RangedNumericItemView;

public class EntityPopulationView extends RangedNumericItemView {

    private DTOEntity entity;

    public EntityPopulationView(DTOEntity entity) {
        super(0, 1000); //TODO
        this.entity = entity;
        title.set(entity.getName());
    }

    public void setEntity(DTOEntity entity) {
        this.entity = entity;
        title.set(entity.getName());
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    protected void bind() {
        super.bind();
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
