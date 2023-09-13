package ui.component.subcomponent.result;

public class EntityInfo {
    private final String entityName;
    private final Integer instanceCount;

    public EntityInfo(String entityName, Integer instanceCount) {
        this.entityName = entityName;
        this.instanceCount = instanceCount;
    }

    public String getEntityName() {
        return entityName;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }
}
