package menu;

public abstract class MenuItem {

    protected final String text;

    public MenuItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
