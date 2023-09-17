package dto.detail.action;

import dto.detail.DTOObject;

public class DTOSecondaryEntity extends DTOObject {

    protected final String conditions;
    protected final String count;

    public DTOSecondaryEntity(String name, String count, String conditions) {
        super(name);
        this.conditions = conditions;
        this.count = count;
    }

    public String getConditions() {
        return conditions;
    }

    public String getCount() {
        return count;
    }
}
