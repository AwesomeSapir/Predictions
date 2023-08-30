package ui.customcomponent.view;

import dto.detail.DTOEntity;
import ui.customcomponent.view.item.NumericItemView;

public class EntityPopulationView extends NumericItemView {

    private DTOEntity entity;

    public EntityPopulationView(DTOEntity entity) {
        this.entity = entity;
        title.set(entity.getName());
        min.set(0);
        max.set(1000); //TODO
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
