package ui.customcomponent.view;

import dto.detail.DTOEntity;
import ui.customcomponent.view.item.NumericItemView;

public class EntityPopulationView extends NumericItemView {

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
    public void initialize() {
        super.initialize();
    }
}
