package ui.customcomponent.view.item;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.ToggleSwitch;

public class BooleanItemView extends InputItemView<Boolean> {

    @FXML protected ToggleSwitch toggleSwitch;
    @FXML protected Label labelText;

    public BooleanItemView() {
        super();
        load(getClass().getResource("/ui/customcomponent/view/item/viewItemBoolean.fxml"));
    }

    @Override
    public void clear() {
        toggleSwitch.selectedProperty().set(true);

    }

    @Override
    protected void bind() {
        super.bind();
        value.bindBidirectional(toggleSwitch.selectedProperty());
        labelText.textProperty().bind(value.asString());
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
