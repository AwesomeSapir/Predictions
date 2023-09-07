package ui.engine;

import javafx.beans.property.*;

public class Progress {

    private final BooleanProperty limited;
    private final DoubleProperty percentage;
    private final LongProperty value;
    private final LongProperty max = new SimpleLongProperty();
    public Progress(Long max) {
        this.limited = new SimpleBooleanProperty(max != null);
        this.value = new SimpleLongProperty(0);
        this.percentage = new SimpleDoubleProperty();
        this.max.setValue(max);
        percentage.bind(value.divide(max != null ? (double) max : -1));
    }

    public void setValue(long value) {
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

    public long getValue() {
        return value.get();
    }

    public LongProperty valueProperty() {
        return value;
    }

    public long getMax() {
        return max.get();
    }

    public LongProperty maxProperty() {
        return max;
    }
}
