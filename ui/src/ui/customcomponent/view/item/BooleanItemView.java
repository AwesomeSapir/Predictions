package ui.customcomponent.view.item;

import com.sun.javafx.binding.BidirectionalBinding;
import javafx.beans.property.SimpleBooleanProperty;
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
        if (value.isBound()){
            value.unbind();
        }
        toggleSwitch.selectedProperty().set(true);
        value.bind(toggleSwitch.selectedProperty());
    }

    @Override
    public void initialize() {
        super.initialize();
        clear();
        labelText.textProperty().bind(value.asString());
    }
}
