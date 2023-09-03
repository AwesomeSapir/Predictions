package ui.component.custom.detail;

import dto.detail.DTOObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public abstract class DetailView<T extends DTOObject> {

    @FXML protected Label labelName;

    protected final StringProperty name = new SimpleStringProperty();

    public void setObject(T object) {
        name.set(object.getName());
    }

    @FXML
    public void initialize(){
        labelName.textProperty().bind(name);
    }

}
