package ui.component.custom.detail.property;

import dto.detail.DTOProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.component.custom.detail.DetailView;

public class PropertyDetailView extends DetailView<DTOProperty> {

    @FXML public Label labelType;
    @FXML public Label labelRange;
    @FXML public Label labelRandom;

    protected final StringProperty type = new SimpleStringProperty();
    protected final StringProperty range = new SimpleStringProperty();
    protected final BooleanProperty random = new SimpleBooleanProperty();

    @Override
    public void setObject(DTOProperty object) {
        super.setObject(object);
        type.set(object.getType());
        range.set(object.getRange().toString());
        random.set(object.isRandomInit());
    }

    @Override
    public void initialize() {
        super.initialize();
        labelType.textProperty().bind(type);
        labelRange.textProperty().bind(range);
        labelRandom.textProperty().bind(random.asString());
    }
}
