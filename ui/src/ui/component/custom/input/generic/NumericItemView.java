package ui.component.custom.input.generic;

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
    protected void randomize() {
        textField.setText(String.valueOf(Double.MIN_VALUE + random.nextDouble() * (Double.MAX_VALUE - Double.MIN_VALUE)));
    }

    @Override
    public Double getValue() {
        return Double.parseDouble(textField.textProperty().get());
    }
}
