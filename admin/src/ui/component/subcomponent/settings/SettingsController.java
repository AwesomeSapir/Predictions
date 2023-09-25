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
    @FXML public ComboBox<Font> comboBoxFont;
    @FXML public ComboBox<FontSize> comboBoxFontSize;
    @FXML private ComboBox<Color> comboBoxColor;
    private final Map<ComboBox<?>, Node> comboToListView = new HashMap<>();
    private StyleManager styleManager;

    @FXML
    public void initialize() {
        styleManager = StyleManager.getInstance();
        comboBoxColor.getItems().setAll(Color.values());
        comboBoxFont.getItems().setAll(Font.values());
        comboBoxFontSize.getItems().setAll(FontSize.values());

        toggleDark.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                //Animations.nudgeX(toggleDark, 8);
                styleManager.changeMode(Mode.DARK);
            } else {
                //Animations.nudgeX(toggleDark, -8);
                styleManager.changeMode(Mode.LIGHT);
            }
        });

        comboBoxColor.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            styleManager.changeColor(newValue);
        }));

        comboBoxFont.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            styleManager.changeFont(newValue);
        }));

        comboBoxFontSize.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            styleManager.changeFontSize(newValue);
        });

        toggleDark.selectedProperty().set(styleManager.getCurrentMode() == Mode.DARK);
        comboBoxColor.getSelectionModel().select(styleManager.getCurrentColor());
        comboBoxFont.getSelectionModel().select(styleManager.getCurrentFont());
        comboBoxFontSize.getSelectionModel().select(styleManager.getCurrentFontSize());

        initComboBoxAnimations(comboBoxColor);
        initComboBoxAnimations(comboBoxFont);
        initComboBoxAnimations(comboBoxFontSize);
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
