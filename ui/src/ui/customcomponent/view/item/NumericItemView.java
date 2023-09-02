package ui.customcomponent.view.item;

import ui.validation.Validator;

public class NumericItemView extends TextInputItemView<Double>{

    public NumericItemView() {
        super(Validator.create().isDouble(), "Invalid input. Must be a number.");
    }

    @Override
    public void clear() {
        textField.setText("0");
    }

    @Override
    public Double getValue() {
        return Double.parseDouble(textField.textProperty().get());
    }
}
