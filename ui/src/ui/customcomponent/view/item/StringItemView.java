package ui.customcomponent.view.item;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import validator.Validator;

public class StringItemView extends InputItemView<String> {

    @FXML private TextField textField;
    @FXML private Label labelError;

    protected SimpleStringProperty title;
    protected Validator validator;

    public StringItemView() {
        super("/ui/customcomponent/view/item/viewItemString.fxml");
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void clear() {
        if(value.isBound()){
            value.unbind();
        }
        value.set("");
        value.bind(textField.textProperty());
    }

    @FXML
    public void initialize(){
        super.initialize();
        isValid = new SimpleBooleanProperty(true);
        clear();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            isValid.set(Validator.validate(newValue).isValidString().isValid());
        });
        labelError.visibleProperty().bind(isValid.not());
        labelError.setText("Invalid characters, only A-Z, a-z, 0-9 are allowed.");
    }
}
