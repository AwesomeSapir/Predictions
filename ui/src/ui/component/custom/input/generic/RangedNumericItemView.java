package ui.component.custom.input.generic;

import dto.detail.DTORange;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.Arrays;
import java.util.Collection;

public class RangedNumericItemView extends InputItemView<Double> {

    @FXML protected Slider sliderAmount;
    @FXML protected Label labelMin;
    @FXML protected Label labelMax;
    @FXML private Label labelValue;

    protected DoubleProperty min;
    protected DoubleProperty max;
    protected BooleanProperty integer;

    public RangedNumericItemView(Number min, Number max, boolean integer){
        super();
        this.min = new SimpleDoubleProperty(min.doubleValue());
        this.max = new SimpleDoubleProperty(max.doubleValue());
        isValid = new SimpleBooleanProperty(true);
        this.integer = new SimpleBooleanProperty(integer);
        load(getClass().getResource("/ui/component/custom/input/generic/viewItemNumeric.fxml"));
    }

    public void setRange(DTORange range){
        setMin(range.getFrom());
        setMax(range.getTo());
    }

    public void setMin(Number min) {
        this.min.set(min.doubleValue());
    }

    public void setMax(Number max) {
        this.max.set(max.doubleValue());
    }

    @Override
    public void clear() {
        sliderAmount.valueProperty().setValue(min.getValue());
    }

    @Override
    public void setValue(Object value) {
        sliderAmount.setValue((Double) value);
    }

    @Override
    protected void randomize() {
        double randNum;
        if(integer.get()){
            randNum = (int)min.get() + random.nextInt((int) (max.get() - min.get() + 1));
        } else {
            randNum = min.get() + random.nextDouble() * (max.get() - min.get());
        }
        sliderAmount.valueProperty().set(randNum);
    }

    @Override
    protected void bind() {
        super.bind();
        String format = "";
        value.setValue(0.0);
        if (integer.get()) {
            sliderAmount.setBlockIncrement(1);
            sliderAmount.setMajorTickUnit(1);
            format += "%.0f";
        } else {
            sliderAmount.setBlockIncrement(0.1);
            sliderAmount.setMajorTickUnit(0.1);
            format += "%.1f";
        }
        value.bind(sliderAmount.valueProperty().asObject());
        labelValue.textProperty().bind(sliderAmount.valueProperty().asString(format));

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

    @Override
    protected Collection<Node> getNodeToShake() {
        return Arrays.asList(sliderAmount, labelValue);
    }

    @FXML
    public void initialize(){
        super.initialize();
    }

}
