package ui.customcomponent.view.item;

import ui.validation.Validator;

public class StringItemView extends TextInputItemView<String>{

    public StringItemView() {
        super(Validator.create().isValidString(), "Invalid input. The possible characters are: A-Z, a-z, 0-9, white space, the following: !?,_-().");
    }

    @Override
    public void clear() {
        textField.setText("");
    }

    @Override
    protected void bind() {
        super.bind();
        value.bind(textField.textProperty());
    }
}
