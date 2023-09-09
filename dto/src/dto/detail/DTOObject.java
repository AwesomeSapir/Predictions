package dto.detail;

public abstract class DTOObject{

    protected final String name;

    public DTOObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
