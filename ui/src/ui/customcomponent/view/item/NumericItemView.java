package ui.customcomponent.view.item;

import dto.detail.DTORange;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class NumericItemView extends InputItemView<Double> {

    @FXML protected Slider sliderAmount;
    @FXML protected TextField textFieldAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;

    protected SimpleDoubleProperty min;
    protected SimpleDoubleProperty max;

    public NumericItemView(double min, double max){
        super();
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        load(getClass().getResource("/ui/customcomponent/view/item/viewItemNumeric.fxml"));
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
        textFieldAmount.textProperty().set(String.format("%.1f", min.getValue()));
    }

    @Override
    protected void bind() {
        super.bind();

        sliderAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
            textFieldAmount.textProperty().set(String.format("%.1f", newValue.doubleValue()));
        });
        textFieldAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                if (Double.parseDouble(newValue) < min.get()) {
                    sliderAmount.valueProperty().set(min.getValue());
                } else if (Double.parseDouble(newValue) > max.get()) {
                    sliderAmount.valueProperty().set(max.getValue());
                } else {
                    sliderAmount.valueProperty().set(Double.parseDouble(newValue));
                }
            }
        });

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

        sliderAmount.minProperty().bind(min);
        sliderAmount.maxProperty().bind(max);
        labelMin.textProperty().bind(min.asString());
        labelMax.textProperty().bind(max.asString());

        value.bindBidirectional(sliderAmount.valueProperty().asObject());
    }

    @FXML
    public void initialize(){
        super.initialize();
    }
}
