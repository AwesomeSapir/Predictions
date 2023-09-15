package ui.component.subcomponent.settings;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import ui.component.custom.node.ToggleSwitch;
import ui.style.*;

import java.util.HashMap;
import java.util.Map;

public class SettingsController {

    @FXML public ToggleSwitch toggleDark;
    @FXML public ComboBox<Font> fontComboBox;
    @FXML private ComboBox<Color> colorComboBox;

    private final Map<ComboBox<?>, Node> comboToListView = new HashMap<>();

    @FXML
    public void initialize() {
        colorComboBox.getItems().setAll(Color.values());
        fontComboBox.getItems().setAll(Font.values());
        toggleDark.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                //Animations.nudgeX(toggleDark, 8);
                StyleManager.changeMode(Mode.DARK);
            } else {
                //Animations.nudgeX(toggleDark, -8);
                StyleManager.changeMode(Mode.LIGHT);
            }
        });

        colorComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            StyleManager.changeColor(newValue);
        }));

        fontComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            StyleManager.changeFont(newValue);
        }));

        toggleDark.selectedProperty().set(StyleManager.getCurrentMode() == Mode.DARK);
        colorComboBox.getSelectionModel().select(StyleManager.getCurrentColor());
        fontComboBox.getSelectionModel().select(StyleManager.getCurrentFont());

        initComboBoxAnimations(colorComboBox);
        initComboBoxAnimations(fontComboBox);
    }

    private void initComboBoxAnimations(ComboBox<?> comboBox){
        comboBox.setOnShowing(event -> {
            if(!comboToListView.containsKey(comboBox)){
                comboToListView.put(comboBox, comboBox.lookup(".list-view"));
            }
            Animations.bounceDown(comboBox);
            Animations.bounceDown(comboToListView.get(comboBox));
        });
        comboBox.setOnHiding(event -> {
            if(!comboToListView.containsKey(comboBox)){
                comboToListView.put(comboBox, comboBox.lookup(".list-view"));
            }
            Animations.bounceUp(comboBox);
            Animations.bounceUp(comboToListView.get(comboBox));
        });
    }

}
