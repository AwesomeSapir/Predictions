package ui.component.custom.node;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class IconButton extends Button {

    protected final StringProperty icon = new SimpleStringProperty("icon-play");
    protected final ImageView iconView = new ImageView();

    public IconButton() {
        this.setGraphic(iconView);
        icon.addListener((observable, oldValue, newValue) -> {
            iconView.getStyleClass().clear();
            iconView.getStyleClass().add(newValue);
        });
    }

    public void setIcon(String icon) {
        this.icon.set(icon);
    }

    public String getIcon() {
        return icon.get();
    }

    public StringProperty iconProperty() {
        return icon;
    }
}
