package dto.detail.action;

import dto.detail.DTOObject;

public class DTOAction extends DTOObject {

    protected final String entityName;

    public DTOAction(String type, String entityName) {
        super(type);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }
}
