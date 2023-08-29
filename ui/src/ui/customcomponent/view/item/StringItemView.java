package ui.customcomponent.view.item;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import validator.Validator;

public class StringItemView extends InputItemView {

    @FXML private TextField textField;
    @FXML private Label labelError;

    protected SimpleStringProperty title;
    protected SimpleBooleanProperty isInputValid;
    protected Validator validator;

    public StringItemView() {
        super("/ui/customcomponent/view/item/viewItemString.fxml");
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @FXML
    public void initialize(){
        super.initialize();
        isInputValid = new SimpleBooleanProperty(true);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            isInputValid.set(Validator.validate(newValue).isValidString().isValid());
            if(!isInputValid.get()){
                textField.textProperty().set(oldValue);
            }
        });
        labelError.visibleProperty().bind(isInputValid.not());
        labelError.setText("Invalid characters, only A-Z, a-z, 0-9 are allowed.");
    }

}
