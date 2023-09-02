package ui.validation;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    List<ValidationRule> rules = new ArrayList<>();

    private Validator(){}

    public static Validator create(){
        return new Validator();
    }

    public boolean validate(Object object){
        boolean result = true;
        for (ValidationRule rule : rules){
            if(!rule.isValid(object)){
                result = false;
            }
        }
        return result;
    }

    public Validator isInteger(){
        rules.add(object -> {
            try {
                Integer.parseInt(String.valueOf(object));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        return this;
    }

    public Validator isDouble(){
        rules.add(object -> {
            try {
                Double.parseDouble(String.valueOf(object));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        return this;
    }

    public Validator isInRange(double from, double to){
        rules.add(object -> {
            try {
                double value = Double.parseDouble(String.valueOf(object));
                return !(value > to) && !(value < from);
            }catch (NumberFormatException e) {
                return false;
            }
        });
        return this;
    }

    public Validator isValidString(){
        rules.add(object -> {
            String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";
            int maxStringSize = 50;
            String input = object.toString();

            for (char c : input.toCharArray()) {
                if (CHARACTERS.indexOf(c) == -1) {
                    return false;
                }
            }
            return input.length() <= maxStringSize;
        });
        return this;
    }

    public Validator isBoolean(){
        rules.add(object -> {
            return object.toString().equalsIgnoreCase("true") || object.toString().equalsIgnoreCase("false");
        });
        return this;
    }
}
