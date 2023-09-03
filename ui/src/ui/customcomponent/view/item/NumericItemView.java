package ui.customcomponent.view.item;

import dto.detail.DTORange;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import validator.Validator;

public class NumericItemView extends InputItemView<Double> {

    @FXML protected Slider sliderAmount;
    @FXML protected TextField textFieldAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;

    @FXML private Label labelErrorNumeric;

    protected SimpleDoubleProperty min;
    protected SimpleDoubleProperty max;

    public NumericItemView(double min, double max){
        super();
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        isValid = new SimpleBooleanProperty(true);
        load(getClass().getResource("/ui/customcomponent/view/item/viewItemNumeric.fxml"));
        labelErrorNumeric.setText("Invalid value. The range values is: '" + min + "-" + max + "'");
    }

    public void setRange(DTORange range){
        setMin(range.getFrom());
        setMax(range.getTo());
    }

    public void setMin(double min) {
        this.min.set(min);
    }

    public void setMax(double max) {
        this.max.set(max);
    }

    @Override
    public void clear() {
        sliderAmount.valueProperty().setValue(min.getValue());
        textFieldAmount.setText(String.format("%.1f", min.getValue()));
    }

    @Override
    protected void bind() {
        super.bind();

        // Create a custom StringConverter for the TextField
        StringConverter<Double> doubleConverter = new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.format("%.1f", object);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return min.getValue(); // Default to min value if parsing fails
                }
            }
        };

        // Create a TextFormatter using the custom converter
        TextFormatter<Double> textFormatter = new TextFormatter<>(doubleConverter, min.getValue());

        // Set the TextFormatter to the TextField
        textFieldAmount.setTextFormatter(textFormatter);

        // Track whether the user is editing in regular text format
        SimpleBooleanProperty isTextFormatEditing = new SimpleBooleanProperty(false);

        // Listener to detect regular text format editing
        textFieldAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isTextFormatEditing.get()) {
                // Update the sliderAmount when the text in textFieldAmount changes
                try {
                    double parsedValue = Double.parseDouble(newValue);
                    if (parsedValue >= min.get() && parsedValue <= max.get()) {
                        sliderAmount.setValue(parsedValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input here if needed
                }
            }
        });

        // Add focus listener to toggle between double and regular text format editing
        textFieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Gain focus, switch to regular text format editing
                isTextFormatEditing.set(true);
            } else {
                // Lose focus, switch back to double format
                isTextFormatEditing.set(false);
                textFieldAmount.setText(doubleConverter.toString(textFormatter.getValue()));
            }
        });

        // Bind style based on isValid
        textFieldAmount.styleProperty().bind(Bindings.when(isValid)
                .then("")
                .otherwise("-fx-text-fill: red; -fx-focus-color: red;"));

        labelErrorNumeric.visibleProperty().bind(isValid.not());

        // Add a listener to update textFieldAmount when sliderAmount changes
        sliderAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!isTextFormatEditing.get()) {
                textFieldAmount.setText(doubleConverter.toString((Double) newValue));
            }
        });

        // Listener to detect changes in textFieldAmount for validation
        textFieldAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double parsedValue = Double.parseDouble(newValue);
                if (parsedValue >= min.get() && parsedValue <= max.get()) {
                    isValid.set(true);
                    sliderAmount.setValue(parsedValue); // Update slider when text is valid
                } else {
                    isValid.set(false);
                }
            } catch (NumberFormatException e) {
                isValid.set(false);
            }
        });

        sliderAmount.minProperty().bind(min);
        sliderAmount.maxProperty().bind(max);
        labelMin.textProperty().bind(min.asString());
        labelMax.textProperty().bind(max.asString());

        value.bind(textFormatter.valueProperty());
    }



    @FXML
    public void initialize(){
        super.initialize();
    }

}
