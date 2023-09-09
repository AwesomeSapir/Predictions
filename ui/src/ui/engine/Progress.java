package ui.engine;

import javafx.beans.property.*;

public class Progress {

    private final BooleanProperty limited;
    private final DoubleProperty percentage;
    private final DoubleProperty value;
    private final IntegerProperty max = new SimpleIntegerProperty();

    public Progress(Integer max) {
        this.limited = new SimpleBooleanProperty(max != null);
        this.value = new SimpleDoubleProperty(0);
        this.percentage = new SimpleDoubleProperty();
        this.max.setValue(max);
        percentage.bind(value.divide(max != null ? max : -1));
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public double getPercentage() {
        return percentage.get();
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }

    public boolean isLimited() {
        return limited.get();
    }

    public BooleanProperty limitedProperty() {
        return limited;
    }

    public double getValue() {
        return value.get();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getMax() {
        return max.get();
    }

    public IntegerProperty maxProperty() {
        return max;
    }
}
