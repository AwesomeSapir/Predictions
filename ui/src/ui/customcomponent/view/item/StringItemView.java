package ui.customcomponent.view.item;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ui.validation.Validator;

public class StringItemView extends InputItemView<String> {

    @FXML private TextField textField;
    @FXML private Label labelError;

    protected SimpleStringProperty title;
    protected Validator validator;

    public StringItemView(Validator validator, String errorMessage) {
        super();
        this.validator = validator;
        load(getClass().getResource("/ui/customcomponent/view/item/viewItemString.fxml"));
        labelError.setText(errorMessage);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void clear() {
        textField.setText("");
    }

    @Override
    protected void bind() {
        super.bind();
        value.bind(textField.textProperty());
        labelError.visibleProperty().bind(isValid.not());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            isValid.set(newValue.isEmpty() || validator.validate(newValue));
        });
    }

    public TextField getTextField() {
        return textField;
    }

    @FXML
    public void initialize(){
        super.initialize();
    }
}
