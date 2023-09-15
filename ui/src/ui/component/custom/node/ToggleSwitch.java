package ui.component.custom.node;

import ui.style.Animations;

public class ToggleSwitch extends org.controlsfx.control.ToggleSwitch {

    public ToggleSwitch() {
        super();
        selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Animations.bounceRight(this);
            } else {
                Animations.bounceLeft(this);
            }
        });
    }
}
