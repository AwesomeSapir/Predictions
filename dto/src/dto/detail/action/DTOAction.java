package dto.detail.action;

import dto.detail.DTOObject;

public class DTOAction extends DTOObject {

    protected final String entityName;
    protected final DTOSecondaryEntity secondaryEntity;

    public DTOAction(String type, String entityName, DTOSecondaryEntity secondaryEntity) {
        super(type);
        this.entityName = entityName;
        this.secondaryEntity = secondaryEntity;
    }

    public String getEntityName() {
        return entityName;
    }

    public DTOSecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }
}
