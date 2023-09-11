package ui.component.custom.input.simulation;

import dto.detail.DTOEntity;
import ui.component.custom.input.generic.RangedNumericItemView;

public class EntityPopulationView extends RangedNumericItemView {

    public EntityPopulationView(DTOEntity entity, int max) {
        super(0, max, true);
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
