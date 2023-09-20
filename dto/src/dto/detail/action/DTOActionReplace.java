package dto.detail.action;

public class DTOActionReplace extends DTOAction{

    private final String create;
    private final String mode;

    public DTOActionReplace(String type, String kill, DTOSecondaryEntity secondaryEntity, String create, String mode) {
        super(type, kill, secondaryEntity);
        this.create = create;
        this.mode = mode;
    }

    public String getCreate() {
        return create;
    }

    public String getMode() {
        return mode;
    }
}
