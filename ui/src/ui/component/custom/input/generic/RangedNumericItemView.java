package ui.component.custom.input.generic;

import dto.detail.DTORange;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class RangedNumericItemView extends InputItemView<Double> {

    @FXML protected Slider sliderAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;
    @FXML private Label labelValue;

    protected SimpleDoubleProperty min;
    protected SimpleDoubleProperty max;

    public RangedNumericItemView(double min, double max){
        super();
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        isValid = new SimpleBooleanProperty(true);
        load(getClass().getResource("/ui/component/custom/input/generic/viewItemNumeric.fxml"));
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
    }

    @Override
    protected void randomize() {
        sliderAmount.valueProperty().set(min.get() + random.nextDouble() * (max.get() - min.get()));
    }

    @Override
    protected void bind() {
        super.bind();
        value.bindBidirectional(sliderAmount.valueProperty().asObject());
        labelValue.textProperty().bind(sliderAmount.valueProperty().asString("%.1f"));

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

    @FXML
    public void initialize(){
        super.initialize();
    }

}
