package validator;

import engine.world.definition.property.PropertyType;
import engine.world.type.Range;

import java.io.Serializable;

public class Validator implements Serializable {

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

    public Validator isValidString(){
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";
        int maxStringSize = 50;

        for (char c : input.toCharArray()) {
            if (CHARACTERS.indexOf(c) == -1) {
                isValid = false;
                break;
            }
        }
        if (input.length() > maxStringSize){
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

    public Validator isCompatibleWith(PropertyType typeCompareWith, String expToCompare){
        if(input.equals(typeCompareWith.toString())) {
            return this;
        }

        switch (typeCompareWith){
            case DECIMAL:
            case STRING:
                isValid = false;
                break;
            case BOOLEAN:
                if(!expToCompare.equals("true") && !expToCompare.equals("false")){
                    isValid = false;
                }
                break;
            case FLOAT:
                if(!input.equals(PropertyType.DECIMAL.toString())){
                    isValid = false;
                }
                break;
        }
        return this;
    }
}
