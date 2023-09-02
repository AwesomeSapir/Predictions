package ui.customcomponent.view;

import dto.detail.DTOEnvironmentVariable;
import ui.customcomponent.view.item.BooleanItemView;
import ui.customcomponent.view.item.InputItemView;
import ui.customcomponent.view.item.NumericItemView;
import ui.customcomponent.view.item.StringItemView;
import ui.validation.Validator;

public class EnvironmentVariableView  {

    private final InputItemView view;
    private DTOEnvironmentVariable environmentVariable;

    public EnvironmentVariableView(DTOEnvironmentVariable environmentVariable) {
        this.environmentVariable = environmentVariable;
        switch (environmentVariable.getType()){
            case "DECIMAL":
            case "FLOAT":
                if(environmentVariable.getRange() != null) {
                    view = new NumericItemView(environmentVariable.getRange().getFrom(), environmentVariable.getRange().getTo());
                } else {
                    view = new StringItemView(Validator.create().isDouble(), "Invalid input. Must be a number.");
                }
                break;
            case "BOOLEAN":
                view = new BooleanItemView();
                break;
            case "STRING":
                view = new StringItemView(Validator.create().isValidString(), "Invalid input. The possible characters are: A-Z, a-z, 0-9, white space, the following: !?,_-().");
                break;
            default:
                throw new IllegalArgumentException("Invalid variable type: " + environmentVariable.getType());
        }
        view.setTitle(environmentVariable.getName());
    }

    public InputItemView getView() {
        return view;
    }
}
