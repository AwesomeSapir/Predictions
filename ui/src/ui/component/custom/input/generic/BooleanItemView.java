package ui.component.custom.input.generic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import ui.component.custom.node.ToggleSwitch;

import java.util.Arrays;
import java.util.Collection;

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
    public void setValue(Object value) {
        toggleSwitch.setSelected((Boolean) value);
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
    protected Collection<Node> getNodeToShake() {
        return Arrays.asList(toggleSwitch, labelValue);
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
