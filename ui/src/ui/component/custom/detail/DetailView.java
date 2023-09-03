package ui.component.custom.detail;

import dto.detail.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DetailView {

    public Label labelTitle;
    public GridPane gridName;
    public Label labelName;

    public GridPane gridEntity;
    public Label labelEntityProperties;

    public GridPane gridProperty;
    public Label labelPropertyType;
    public Label labelPropertyRange;
    public Label labelPropertyRandom;

    public GridPane gridRule;
    public Label labelRuleTicks;
    public Label labelRuleProbability;
    public Label labelRuleActions;

    public GridPane gridTermination;
    public Label labelTerminationTicks;
    public Label labelTerminationSeconds;
    protected DTOObject loadedObject;

    protected final StringProperty name = new SimpleStringProperty();
    protected final StringProperty properties = new SimpleStringProperty();
    protected final StringProperty type = new SimpleStringProperty();
    protected final StringProperty range = new SimpleStringProperty();
    protected final BooleanProperty random = new SimpleBooleanProperty();
    protected final LongProperty ticks = new SimpleLongProperty();
    protected final LongProperty seconds = new SimpleLongProperty();
    protected final DoubleProperty probability = new SimpleDoubleProperty();
    protected final StringProperty actions = new SimpleStringProperty();

    public void setEntity(DTOEntity object) {
        resetVisibility();
        updateVisibility(gridName, gridEntity);

        this.loadedObject = object;
        labelTitle.setText("Entity");
        name.set(object.getName());
        properties.set(object.getProperties().toString());
    }

    public void setProperty(DTOProperty object) {
        resetVisibility();
        updateVisibility(gridName, gridProperty);

        this.loadedObject = object;
        labelTitle.setText("Property");
        name.set(object.getName());
        type.set(object.getType());
        range.set(object.getRange()==null ? "none" : object.getRange().getFrom() + " " + object.getRange().getTo());
        random.set(object.isRandomInit());
    }

    public void setRule(DTORule object) {
        resetVisibility();
        updateVisibility(gridName, gridRule);


        this.loadedObject = object;
        labelTitle.setText("Rule");
        name.set(object.getName());
        ticks.set(object.getTicks());
        probability.set(object.getProbability());
        actions.set(object.getActions().toString());
    }

    public void setTermination(DTOTermination object) {
        resetVisibility();
        updateVisibility(gridTermination);

        //this.loadedObject = object;
        labelTitle.setText("Property");
        ticks.set(object.getTicks());
        seconds.set(object.getSeconds());
    }

    private void resetVisibility() {
        gridName.setVisible(false);
        gridEntity.setVisible(false);
        gridProperty.setVisible(false);
        gridRule.setVisible(false);
        gridTermination.setVisible(false);
    }

    private void updateVisibility(GridPane... grids) {
        for (GridPane grid : grids) {
            grid.setVisible(true);
        }
    }

    @FXML
    public void initialize(){
        Bindings.bindBidirectional(gridName.visibleProperty(), gridName.managedProperty());
        Bindings.bindBidirectional(gridEntity.visibleProperty(), gridEntity.managedProperty());
        Bindings.bindBidirectional(gridProperty.visibleProperty(), gridProperty.managedProperty());
        Bindings.bindBidirectional(gridRule.visibleProperty(), gridRule.managedProperty());
        Bindings.bindBidirectional(gridTermination.visibleProperty(), gridTermination.managedProperty());

        resetVisibility();

        labelName.textProperty().bind(name);

        labelEntityProperties.textProperty().bind(properties);

        labelPropertyType.textProperty().bind(type);
        labelPropertyRange.textProperty().bind(range);
        labelPropertyRandom.textProperty().bind(random.asString());

        labelRuleTicks.textProperty().bind(ticks.asString());
        labelRuleProbability.textProperty().bind(probability.asString());
        labelRuleActions.textProperty().bind(actions);

        labelTerminationTicks.textProperty().bind(ticks.asString());
        labelTerminationSeconds.textProperty().bind(seconds.asString());
    }

}
