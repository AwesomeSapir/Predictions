package ui.customcomponent.view.item;

import dto.detail.DTORange;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

        sliderAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
            textFieldAmount.textProperty().set(String.valueOf(newValue.doubleValue()));
        });
        // Create a custom StringConverter for the TextField
        StringConverter<Double> converter = new StringConverter<Double>() {
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
        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, min.getValue());

        // Set the TextFormatter to the TextField
        textFieldAmount.setTextFormatter(textFormatter);

        textFieldAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            isValid.set(Validator.validate(newValue).isInRange(min.doubleValue(), max.doubleValue()).isValid());
        });

        // Bind style based on isValid
        textFieldAmount.styleProperty().bind(Bindings.when(isValid)
                .then("")
                .otherwise("-fx-text-fill: red; -fx-focus-color: red;"));

        labelErrorNumeric.visibleProperty().bind(isValid.not());

        sliderAmount.minProperty().bind(min);
        sliderAmount.maxProperty().bind(max);
        labelMin.textProperty().bind(min.asString());
        labelMax.textProperty().bind(max.asString());

        //value.bind(textFormatter.valueProperty());
    }



    @FXML
    public void initialize(){
        super.initialize();
    }

}
