package ui.engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntityInfo {
    private final StringProperty entityName;
    private final IntegerProperty instanceCount;
    private final IntegerProperty initialCount;

    public EntityInfo(String entityName, int initialCount) {
        this.entityName = new SimpleStringProperty(entityName);
        this.initialCount = new SimpleIntegerProperty(initialCount);
        this.instanceCount = new SimpleIntegerProperty(initialCount);
    }

    public String getEntityName() {
        return entityName.get();
    }

    public StringProperty entityNameProperty() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName.set(entityName);
    }

    public int getInstanceCount() {
        return instanceCount.get();
    }

    public IntegerProperty instanceCountProperty() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount.set(instanceCount);
    }

    public int getInitialCount() {
        return initialCount.get();
    }

    public IntegerProperty initialCountProperty() {
        return initialCount;
    }

    @Override
    public String toString() {
        return entityName.get();
    }
}
