package ui.customcomponent.view.item;

import dto.detail.DTORange;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

public class NumericItemView extends InputItemView {

    @FXML protected Slider sliderAmount;
    @FXML protected TextField textFieldAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;

    private SimpleDoubleProperty min;
    private SimpleDoubleProperty max;
    private SimpleDoubleProperty amount;

    public NumericItemView() {
        super("/ui/customcomponent/view/item/viewItemNumeric.fxml");
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

    public double getAmount() {
        return amount.get();
    }

    @FXML
    public void initialize(){
        super.initialize();

        min = new SimpleDoubleProperty(0);
        max = new SimpleDoubleProperty(1000);
        amount = new SimpleDoubleProperty((min.get() + max.get())/2);

        min.addListener((observable, oldValue, newValue) -> {
            if(amount.lessThan(newValue.doubleValue()).get()){
                amount.setValue(newValue);
            }
        });
        max.addListener((observable, oldValue, newValue) -> {
            if(amount.greaterThan(newValue.doubleValue()).get()){
                amount.setValue(newValue);
            }
        });

        Bindings.bindBidirectional(amount, sliderAmount.valueProperty());
        Bindings.bindBidirectional(textFieldAmount.textProperty(), amount, new NumberStringConverter());
        sliderAmount.minProperty().bind(min);
        sliderAmount.maxProperty().bind(max);
        labelMin.textProperty().bind(min.asString());
        labelMax.textProperty().bind(max.asString());
    }
}
