package ui.customcomponent.view.item;

import dto.detail.DTORange;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

public class NumericItemView extends InputItemView<Double> {

    @FXML protected Slider sliderAmount;
    @FXML protected TextField textFieldAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;

    protected SimpleDoubleProperty min;
    protected SimpleDoubleProperty max;

    public NumericItemView() {
        super("/ui/customcomponent/view/item/viewItemNumeric.fxml");
    }

    public void setRange(DTORange range){
        System.out.println("setRange");
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
        System.out.println("in numeric clear");
        if(value.isBound()){
            value.unbindBidirectional(sliderAmount.valueProperty().asObject());
            textFieldAmount.textProperty().unbindBidirectional(value);
        }
        value.set(min.getValue());
        value.bindBidirectional(sliderAmount.valueProperty().asObject());
        textFieldAmount.textProperty().bindBidirectional(value, new DoubleStringConverter());
        System.out.println("set value to " + min.get());
    }

    @FXML
    public void initialize(){
        super.initialize();
        min = new SimpleDoubleProperty(Double.MIN_VALUE);
        max = new SimpleDoubleProperty(Double.MAX_VALUE);

        clear();

        sliderAmount.minProperty().bind(min);
        sliderAmount.maxProperty().bind(max);
        labelMin.textProperty().bind(min.asString());
        labelMax.textProperty().bind(max.asString());

        min.addListener((observable, oldValue, newValue) -> {
            if(value.get() < newValue.doubleValue()){
                value.setValue(newValue.doubleValue());
            }
        });
        max.addListener((observable, oldValue, newValue) -> {
            if(value.get() > newValue.doubleValue()){
                value.setValue(newValue.doubleValue());
            }
        });
    }
}
