package ui.component.custom.detail;

import dto.detail.*;
import dto.detail.action.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DetailView {

    public Label labelTitle;
    public GridPane gridData;

    protected DTOObject loadedObject;

    private int currentRow = 0;

    public void addRow(String key, Object value){
        Label labelKey = new Label(key);
        Label labelValue = new Label(value.toString());
        gridData.addRow(currentRow, labelKey, labelValue);
        currentRow++;
    }

    public void setEntity(DTOEntity object) {
        reset();

        this.loadedObject = object;
        labelTitle.setText("Entity");

        addRow("Name", object.getName());
        addRow("Properties", object.getProperties());
    }

    public void setProperty(DTOProperty object) {
        reset();

        this.loadedObject = object;
        labelTitle.setText("Property");

        addRow("Name", object.getName());
        addRow("Type", object.getType());
        addRow("Range", object.getRange() == null ? "none" : object.getRange().getFrom() + " - " + object.getRange().getTo());
        addRow("Random?", object.isRandomInit());
    }

    public void setEnvVariable(DTOEnvironmentVariable object) {
        reset();

        this.loadedObject = object;
        labelTitle.setText("Environment Variable");

        addRow("Name", object.getName());
        addRow("Type", object.getType());
        addRow("Range", object.getRange() == null ? "none" : object.getRange().getFrom() + " - " + object.getRange().getTo());
    }

    public void setRule(DTORule object) {
        reset();

        this.loadedObject = object;
        labelTitle.setText("Rule");

        addRow("Name", object.getName());
        addRow("Ticks", object.getTicks());
        addRow("Probability", object.getProbability());
        addRow("Actions", object.getActions());
    }

    public void setTermination(DTOTermination object) {
        reset();

        this.loadedObject = object;
        labelTitle.setText("Termination");

        if (object.getCondition("TICKS") != null) {
            addRow("Ticks", object.getCondition("TICKS").getCondition());
        }
        if (object.getCondition("SECONDS") != null) {
            addRow("Seconds", object.getCondition("SECONDS").getCondition());
        }
        if (object.getCondition("USER") != null) {
            addRow("User", object.getCondition("USER").getCondition());
        }
    }

    public void setGrid(DTOGrid object) {
        reset();

        labelTitle.setText("Grid");

        addRow("Rows", object.getRows());
        addRow("Columns", object.getCols());
    }

    public void setActionGeneric(DTOAction object){
        setActionGeneric(object, "Primary Entity");
    }

    public void setActionGeneric(DTOAction object, String primaryKey){
        reset();

        labelTitle.setText("Action " + object.getName());

        addRow(primaryKey, object.getEntityName());
        if(object.getSecondaryEntity() != null) {
            addRow("Secondary Entity", object.getSecondaryEntity().getName());
        }
    }

    public void setAction(DTOActionCalc object){
        setActionGeneric(object);

        addRow("Result Property", object.getResultPropertyName());
        addRow("Arg 1", object.getArg1());
        addRow("Arg 2", object.getArg2());
    }

    public void setAction(DTOActionCondition object){
        setActionGeneric(object);

        addRow("Conditions", object.getConditions());
        addRow("Then", object.getActionsThen());
        if(!object.getActionsElse().isEmpty()) {
            addRow("Else", object.getActionsElse());
        }
    }

    public void setAction(DTOActionProximity object){
        setActionGeneric(object, "Source");

        addRow("Target", object.getTarget());
        addRow("Depth", object.getDepth());
        addRow("Actions", object.getActions());
    }

    public void setAction(DTOActionReplace object){
        setActionGeneric(object, "Kill");

        addRow("Create", object.getCreate());
        addRow("Mode", object.getMode());
    }

    public void setAction(DTOActionValue object){
        setActionGeneric(object);

        addRow("Property", object.getPropertyName());
        addRow("By", object.getValue());
    }

    public void setAction(DTOAction object){
        if (object instanceof DTOActionCalc) {
            setAction((DTOActionCalc) object);
            return;
        }
        if (object instanceof DTOActionCondition) {
            setAction((DTOActionCondition) object);
            return;
        }
        if (object instanceof DTOActionProximity) {
            setAction((DTOActionProximity) object);
            return;
        }
        if (object instanceof DTOActionReplace) {
            setAction((DTOActionReplace) object);
            return;
        }
        if (object instanceof DTOActionValue) {
            setAction((DTOActionValue) object);
        }
    }

    private void reset() {
        gridData.getChildren().clear();
        currentRow = 0;
    }

    @FXML
    public void initialize() {

    }
}
