package ui.component.custom.input.generic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.ToggleSwitch;

public class BooleanItemView extends InputItemView<Boolean> {

    @FXML protected ToggleSwitch toggleSwitch;
    @FXML protected Label labelValue;

    public BooleanItemView() {
        super();
        load(getClass().getResource("/ui/component/custom/input/generic/viewItemBoolean.fxml"));
    }

    @Override
    public void clear() {
        toggleSwitch.selectedProperty().set(true);
    }

    @Override
    protected void bind() {
        super.bind();
        value.bindBidirectional(toggleSwitch.selectedProperty());
        labelValue.textProperty().bind(value.asString());
    }

    @Override
    protected void randomize() {
        toggleSwitch.selectedProperty().set(random.nextBoolean());
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
