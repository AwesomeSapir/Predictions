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
        super();
        isValid = new SimpleBooleanProperty(true);
        load(getClass().getResource("/ui/customcomponent/view/item/viewItemString.fxml"));
        labelError.setText("Invalid input. The possible characters are: A-Z, a-z, 0-9, white space, the following: !?,_-().");
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void clear() {
        textField.textProperty().set("");
    }

    @Override
    protected void bind() {
        super.bind();
        value.bindBidirectional(textField.textProperty());
        labelError.visibleProperty().bind(isValid.not());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            isValid.set(Validator.validate(newValue).isValidString().isValid());
        });
    }

    @FXML
    public void initialize(){
        super.initialize();
    }
}
