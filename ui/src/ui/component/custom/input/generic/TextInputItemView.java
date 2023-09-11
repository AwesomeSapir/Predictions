package ui.component.custom.input.generic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ui.validation.Validator;

import java.util.Collection;
import java.util.Collections;

public abstract class TextInputItemView<T> extends InputItemView<T> {

    @FXML protected TextField textField;
    @FXML protected Label labelError;
    protected Validator validator;

    public TextInputItemView(Validator validator, String errorMessage) {
        super();
        this.validator = validator;
        load(getClass().getResource("/ui/component/custom/input/generic/viewItemString.fxml"));
        labelError.setText(errorMessage);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    protected void bind() {
        super.bind();
        labelError.visibleProperty().bind(isValid.not());
        labelError.managedProperty().bind(labelError.visibleProperty());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            isValid.set(newValue.isEmpty() || validator.validate(newValue));
        });
    }

    public TextField getTextField() {
        return textField;
    }

    @Override
    protected Collection<Node> getNodeToShake() {
        return Collections.singletonList(textField);
    }

    @FXML
    public void initialize(){
        super.initialize();
    }
}
