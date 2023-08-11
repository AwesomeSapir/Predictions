package validator;

import world.Context;
import world.definition.property.PropertyDefinition;
import world.definition.property.PropertyType;
import world.expression.FunctionType;
import world.type.Range;

import java.util.Objects;

public class Validator {

    private boolean isValid = true;
    private final String input;

    private Validator(String input){
        this.input = input;
    }

    public static Validator validate(String input){
        return new Validator(input);
    }

    public boolean isValid(){
        return isValid;
    }

    public Validator isWholeInteger(double number)
    {

        // Convert double value
        // of N to integer
        long X = Math.round(number);
        double temp = number - X;

        // If N is not equivalent
        // to any integer
        if (!(temp > 0)) {
        isValid = false;
    }
        return this;
    }

    public Validator isInteger(){
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return this;
    }

    public Validator isDouble(){
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return this;
    }

    public Validator isPropertyExist(Context context, String propertyName) {
        try {
            context.getPrimaryEntityDefinition().getProperties().get(propertyName);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return this;
    }
    public Validator isPropertyTypeNumeric(Context context,String propertyName){
           String type = context.getPrimaryEntityDefinition().getProperties().get(propertyName).getType().toString();
           if(!type.equals("DECIMAL") && !type.equals("FLOAT"))
               isValid = false;
        return this;
        }

    public Validator isEntityExist(Context context,String entityName)  {
       if(context.getPrimaryEntityDefinition().getName().equals("entityName")) {
           isValid = false;
       }
       return this;
    }

    public Validator isValidString(String string){
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";
        int maxStringSize = 50;

        for (char c : string.toCharArray()) {
            if (CHARACTERS.indexOf(c) == -1) {
                isValid = false;
                break;
            }
        }
        if (string.length() > maxStringSize){
            isValid = false;
        }
        return this;
    }
    public Validator isInRange(double from, double to){
        if (isDouble().isValid) {
            double value = Double.parseDouble(input);
            if(value > to || value < from){
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return this;
    }

    public Validator isInRange(Range range){
        if (isDouble().isValid) {
            double value = Double.parseDouble(input);
            if(value > range.to() || value < range.from()){
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return this;
    }

    public Validator isBoolean(){
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
            isValid = false;
        }
        return this;
    }

    public Validator isOnlyLetters(){
        if(!input.matches("^[a-zA-Z]+$")){
            isValid = false;
        }
        return this;
    }
}

