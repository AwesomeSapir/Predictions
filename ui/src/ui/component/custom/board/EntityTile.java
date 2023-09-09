package ui.component.custom.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EntityTile extends Rectangle {
    private String entityName;

    public EntityTile() {
        setFill(Color.valueOf("#263238"));
    }

    public void setEntity(String entityName) {
        this.entityName = entityName;
        setFill(Color.valueOf("#263238"));
    }

    public void setEntity(String entityName, Color color) {
        this.entityName = entityName;
        setFill(color);
    }
}
