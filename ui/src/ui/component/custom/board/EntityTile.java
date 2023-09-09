package ui.component.custom.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EntityTile extends Circle {

    public EntityTile() {
        super(5);
        setFill(Color.valueOf("#263238"));
    }

    public void setEmpty() {
        getStyleClass().clear();setFill(Color.valueOf("#263238"));
    }

    public void setEntity(String color) {
        getStyleClass().clear();
        getStyleClass().add("color-"+color);
    }
}
