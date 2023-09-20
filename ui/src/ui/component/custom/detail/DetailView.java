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

    public GridPane gridRange;
    public Label labelRange;

    public GridPane gridRandom;
    public Label labelRandom;

    public GridPane gridRule;
    public Label labelRuleTicks;
    public Label labelRuleProbability;
    public Label labelRuleActions;

    public GridPane gridTermination;
    public Label labelTerminationTicks;
    public Label labelTerminationSeconds;
    public Label labelTerminationUser;

    public GridPane gridGrid;
    public Label labelRows;
    public Label labelCols;

    protected DTOObject loadedObject;

    protected final StringProperty name = new SimpleStringProperty();
    protected final StringProperty properties = new SimpleStringProperty();
    protected final StringProperty type = new SimpleStringProperty();
    protected final StringProperty range = new SimpleStringProperty();
    protected final BooleanProperty random = new SimpleBooleanProperty();
    protected final StringProperty ticks = new SimpleStringProperty();
    protected final StringProperty seconds = new SimpleStringProperty();
    protected final StringProperty user = new SimpleStringProperty();
    protected final DoubleProperty probability = new SimpleDoubleProperty();
    protected final StringProperty actions = new SimpleStringProperty();
    protected final StringProperty rows = new SimpleStringProperty();
    protected final StringProperty cols = new SimpleStringProperty();

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
        updateVisibility(gridName, gridProperty, gridRange, gridRandom);

        this.loadedObject = object;
        labelTitle.setText("Property");
        name.set(object.getName());
        type.set(object.getType());
        range.set(object.getRange() == null ? "none" : object.getRange().getFrom() + " " + object.getRange().getTo());
        random.set(object.isRandomInit());
    }

    public void setEnvVariable(DTOEnvironmentVariable object) {
        resetVisibility();
        updateVisibility(gridName, gridProperty, gridRange);

        this.loadedObject = object;
        labelTitle.setText("Environment Variable");
        name.set(object.getName());
        type.set(object.getType());
        range.set(object.getRange() == null ? "none" : object.getRange().getFrom() + " " + object.getRange().getTo());
    }

    public void setRule(DTORule object) {
        resetVisibility();
        updateVisibility(gridName, gridRule);


        this.loadedObject = object;
        labelTitle.setText("Rule");
        name.set(object.getName());
        ticks.set(String.valueOf(object.getTicks()));
        probability.set(object.getProbability());
        actions.set(object.getActions().toString());
    }

    public void setTermination(DTOTermination object) {
        resetVisibility();
        updateVisibility(gridTermination);

        //this.loadedObject = object;
        labelTitle.setText("Property");
        if (object.getCondition("TICKS") != null) {
            ticks.set(object.getCondition("TICKS").getCondition().toString());
        } else {
            ticks.set("N/A");
        }
        if (object.getCondition("SECONDS") != null) {
            seconds.set(object.getCondition("SECONDS").getCondition().toString());
        } else {
            seconds.set("N/A");
        }
        if (object.getCondition("USER") != null) {
            user.set(object.getCondition("USER").getCondition().toString());
        } else {
            user.set("N/A");
        }
    }

    public void setGrid(DTOGrid object) {
        resetVisibility();
        updateVisibility(gridGrid);

        labelTitle.setText("Grid");
        rows.set(String.valueOf(object.getRows()));
        cols.set(String.valueOf(object.getCols()));
    }

    private void resetVisibility() {
        gridName.setVisible(false);
        gridEntity.setVisible(false);
        gridProperty.setVisible(false);
        gridRange.setVisible(false);
        gridRandom.setVisible(false);
        gridRule.setVisible(false);
        gridTermination.setVisible(false);
        gridGrid.setVisible(false);
    }

    private void updateVisibility(GridPane... grids) {
        for (GridPane grid : grids) {
            grid.setVisible(true);
        }
    }

    private void bindVisibility(GridPane... grids) {
        for (GridPane grid : grids) {
            Bindings.bindBidirectional(grid.visibleProperty(), grid.managedProperty());
        }
    }

    @FXML
    public void initialize() {
        bindVisibility(gridName, gridEntity, gridProperty, gridRange, gridRandom, gridRule, gridTermination, gridGrid);
        resetVisibility();

        labelName.textProperty().bind(name);

        labelEntityProperties.textProperty().bind(properties);

        labelPropertyType.textProperty().bind(type);
        labelRange.textProperty().bind(range);
        labelRandom.textProperty().bind(random.asString());

        labelRuleTicks.textProperty().bind(ticks);
        labelRuleProbability.textProperty().bind(probability.asString());
        labelRuleActions.textProperty().bind(actions);

        labelTerminationTicks.textProperty().bind(ticks);
        labelTerminationSeconds.textProperty().bind(seconds);
        labelTerminationUser.textProperty().bind(user);

        labelRows.textProperty().bind(rows);
        labelCols.textProperty().bind(cols);
    }
}
