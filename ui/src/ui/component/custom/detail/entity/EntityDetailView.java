package ui.component.custom.detail.entity;

import dto.detail.DTOEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.component.custom.detail.DetailView;

public class EntityDetailView extends DetailView<DTOEntity> {

    @FXML protected Label labelProperties;

    protected final StringProperty properties = new SimpleStringProperty();

    public void setObject(DTOEntity object) {
        super.setObject(object);
        properties.set(object.getProperties().toString());
    }

    @FXML
    public void initialize(){
        super.initialize();
        labelProperties.textProperty().bind(properties);
    }

}
