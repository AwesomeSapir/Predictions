package ui.component.custom.node;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ui.style.Animations;

public class ToggleIconButton extends IconButton{

    private final ObjectProperty<State> state = new SimpleObjectProperty<>(State.ON);
    private final StringProperty iconOn = new SimpleStringProperty();
    private final StringProperty iconOff = new SimpleStringProperty();
    private final ObjectProperty<EventHandler<ActionEvent>> onActionOn = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onActionOff = new SimpleObjectProperty<>();

    public ToggleIconButton() {
        this.setOnAction(event -> toggle());
        this.state.addListener((observable, oldValue, newValue) -> {
            updateIcon();
            EventHandler<ActionEvent> handler;
            if(newValue == State.ON){
                handler = getOnActionOn();
            } else {
                handler = getOnActionOff();
            }
            if (handler != null) {
                handler.handle(new ActionEvent(this, null));
            }
        });

        iconOn.addListener((observable, oldValue, newValue) -> updateIcon());
        iconOff.addListener((observable, oldValue, newValue) -> updateIcon());
    }

    public State getState() {
        return state.get();
    }

    public ObjectProperty<State> stateProperty() {
        return state;
    }

    public void setState(State state) {
        this.state.set(state);
    }

    public String getIconOn() {
        return iconOn.get();
    }

    public StringProperty iconOnProperty() {
        return iconOn;
    }

    public void setIconOn(String iconOn) {
        this.iconOn.set(iconOn);
    }

    public String getIconOff() {
        return iconOff.get();
    }

    public StringProperty iconOffProperty() {
        return iconOff;
    }

    public void setIconOff(String iconOff) {
        this.iconOff.set(iconOff);
    }

    public EventHandler<ActionEvent> getOnActionOn() {
        return onActionOn.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionOnProperty() {
        return onActionOn;
    }

    public void setOnActionOn(EventHandler<ActionEvent> onActionOn) {
        this.onActionOn.set(onActionOn);
    }

    public EventHandler<ActionEvent> getOnActionOff() {
        return onActionOff.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionOffProperty() {
        return onActionOff;
    }

    public void setOnActionOff(EventHandler<ActionEvent> onActionOff) {
        this.onActionOff.set(onActionOff);
    }

    private void updateIcon() {
        Transition spinFirst = Animations.spin(iconView, 0, 180);
        Transition spinSecond = Animations.spin(iconView, 180, 180);
        spinFirst.setOnFinished(event -> {
            if (state.get() == State.ON) {
                setIcon(iconOn.get());
            } else {
                setIcon(iconOff.get());
            }
            spinSecond.playFromStart();
        });

        spinFirst.play();
    }

    public void toggle() {
        if(state.get() == State.ON){
            state.set(State.OFF);
        } else {
            state.set(State.ON);
        }
    }

}
