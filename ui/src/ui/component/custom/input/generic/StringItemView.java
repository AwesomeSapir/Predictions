package ui.component.custom.input.generic;

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
    protected void randomize() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";
        int maxStringSize = 50;
        int size = random.nextInt(maxStringSize + 1); // Random size up to 50
        StringBuilder randomString = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomString.append(randomChar);
        }
        textField.textProperty().set(randomString.toString());
    }

    @Override
    protected void bind() {
        super.bind();
        value.bind(textField.textProperty());
    }
}
