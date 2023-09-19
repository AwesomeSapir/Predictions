package ui.engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntityInfo {
    private final StringProperty entityName;
    private IntegerProperty instanceCount;

    public EntityInfo(String entityName, int instanceCount) {
        this.entityName = new SimpleStringProperty(entityName);
        this.instanceCount = new SimpleIntegerProperty(instanceCount);
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
}
