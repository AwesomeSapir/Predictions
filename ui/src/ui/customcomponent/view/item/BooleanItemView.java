package ui.customcomponent.view.item;

import com.sun.javafx.binding.BidirectionalBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.ToggleSwitch;

public class BooleanItemView extends InputItemView {

    @FXML protected ToggleSwitch toggleSwitch;
    @FXML protected Label labelText;

    protected SimpleBooleanProperty isEnabled;

    public BooleanItemView() {
        super("/ui/customcomponent/view/item/viewItemBoolean.fxml");
    }

    public boolean isEnabled() {
        return isEnabled.get();
    }

    @Override
    public void initialize() {
        super.initialize();
        isEnabled = new SimpleBooleanProperty(true);
        BidirectionalBinding.bind(toggleSwitch.selectedProperty(), isEnabled);
        labelText.textProperty().bind(isEnabled.asString());
    }
}
