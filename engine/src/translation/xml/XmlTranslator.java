package translation.xml;

import com.sun.istack.internal.NotNull;
import engine.prd.*;
import validator.Validator;
import world.World;
import world.definition.entity.EntityDefinition;
import world.definition.property.*;
import world.expression.*;
import world.instance.entity.EntityInstance;
import world.instance.environment.ActiveEnvironment;
import world.instance.environment.EnvironmentManager;
import world.instance.property.PropertyInstance;
import world.rule.Activation;
import world.rule.Rule;
import world.rule.action.Action;
import world.rule.action.ActionType;
import world.rule.action.type.ActionKill;
import world.rule.action.type.calculation.ActionCalc;
import world.rule.action.type.calculation.ActionDivide;
import world.rule.action.type.calculation.ActionMultiply;
import world.rule.action.type.condition.*;
import world.rule.action.type.value.ActionDecrease;
import world.rule.action.type.value.ActionIncrease;
import world.rule.action.type.value.ActionSet;
import world.termination.BySecond;
import world.termination.ByTicks;
import world.termination.Termination;
import world.type.Range;
import world.value.generator.ValueGenerator;
import world.value.generator.ValueGeneratorFactory;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTranslator implements Translator{

    private final PRDWorld prdWorld;
    private World world;
    private EnvironmentManager environmentManager;
    private ActiveEnvironment activeEnvironment;
    private EntityDefinition primaryEntityDefinition;
    private List<EntityInstance> entityInstances = new ArrayList<>();
    private Termination termination;

    public XmlTranslator(PRDWorld prdWorld) {
        this.prdWorld = prdWorld;
    }

    @Override
    public World getWorld() throws InvalidClassException {
        environmentManager = getEnvironmentManager(prdWorld.getPRDEvironment());

        activeEnvironment = environmentManager.createActiveEnvironment();
        activeEnvironment.initProperties(environmentManager.getVariables());

        primaryEntityDefinition = getEntityDefinition(prdWorld.getPRDEntities().getPRDEntity().get(0));

        // Entity instances initialization
        for (int i = 0; i < primaryEntityDefinition.getPopulation(); i++) {
            EntityInstance entityInstance = new EntityInstance(primaryEntityDefinition, i + 1);
            entityInstance.initProperties();
            entityInstances.add(entityInstance);
        }

        termination = getTermination(prdWorld.getPRDTermination());

        Map<String, Rule> rules = new HashMap<>();
        for (PRDRule rule : prdWorld.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), getRule(rule));
        }

        world = new World(environmentManager, activeEnvironment, primaryEntityDefinition, entityInstances, rules, termination);
        return world;
    }

    public Rule getRule(PRDRule prdObject) throws InvalidClassException {
        List<Action> actions = new ArrayList<>();
        String name = prdObject.getName();
        Activation activation = getActivation(prdObject.getPRDActivation());
        for (PRDAction prdAction : prdObject.getPRDActions().getPRDAction()) {
            actions.add(getAction(prdAction));
        }
        return new Rule(name, activation, actions);
    }

    public Activation getActivation(PRDActivation prdObject){
        Integer ticks = prdObject.getTicks();
        Double probability = prdObject.getProbability();
        return new Activation(ticks, probability);
    }

    public ActionCondition getActionCondition(PRDAction prdObject) throws InvalidClassException {
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        MultiCondition conditions = getMultiCondition(prdObject.getPRDCondition());
        List<Action> actionsThen = new ArrayList<>();
        List<Action> actionsElse = new ArrayList<>();
        for (PRDAction prdThenAction : prdObject.getPRDThen().getPRDAction()) {
            actionsThen.add(getAction(prdThenAction));
        }
        for (PRDAction prdElseAction : prdObject.getPRDElse().getPRDAction()) {
            actionsElse.add(getAction(prdElseAction));
        }
        return new ActionCondition(actionType, entityName, conditions, actionsThen, actionsElse);
    }

    public MultiCondition getMultiCondition(PRDCondition prdObject) throws InvalidClassException {
        Logical logical;
        List<Condition> subConditions = new ArrayList<>();
        Singularity singularity = Singularity.valueOf(prdObject.getSingularity());
        if (singularity == Singularity.multiple){
            logical = Logical.valueOf(prdObject.getLogical());
            for (PRDCondition prdCondition : prdObject.getPRDCondition()){
                switch (Singularity.valueOf(prdCondition.getSingularity())) {
                    case single:
                        subConditions.add(getSingleCondition(prdObject));
                        break;
                    case multiple:
                        subConditions.add(getMultiCondition(prdCondition));
                        break;
                }
            }
        } else {
            logical = Logical.and;
            subConditions.add(getSingleCondition(prdObject));
        }
        return new MultiCondition(logical, subConditions);
    }

    public SingleCondition getSingleCondition(PRDCondition prdObject) throws InvalidClassException {
        Operator operator = Operator.fromDRP(prdObject.getOperator());
        String propertyName = prdObject.getProperty();
        PropertyDefinition propertyDefinition = primaryEntityDefinition.getProperties().get(propertyName);
        if(propertyDefinition == null){
            throw new IllegalArgumentException("Property '" + propertyName + "' referenced in SingleCondition does not exist.");
        }
        Expression value = getExpression(prdObject.getValue(), propertyDefinition);
        return new SingleCondition(operator, propertyName, value);
    }

    public ActionIncrease getActionIncrease(PRDAction prdObject) throws InvalidClassException {
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy(), primaryEntityDefinition.getProperties().get(propertyName));
        return new ActionIncrease(actionType, entityName, propertyName, value);
    }

    public ActionDecrease getActionDecrease(PRDAction prdObject) throws InvalidClassException {
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy(), primaryEntityDefinition.getProperties().get(propertyName));
        return new ActionDecrease(actionType, entityName, propertyName, value);
    }

    public ActionKill getActionKill(PRDAction prdObject){
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        return new ActionKill(actionType, entityName);
    }

    public ActionCalc getActionCalc(PRDAction prdObject) throws InvalidClassException {
        ActionCalc action;
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String resultPropertyName = prdObject.getProperty();
        PropertyDefinition propertyDefinition = primaryEntityDefinition.getProperties().get(resultPropertyName);

        if (prdObject.getPRDMultiply() != null) {
            Expression arg1 = getExpression(prdObject.getPRDMultiply().getArg1(), propertyDefinition);
            Expression arg2 = getExpression(prdObject.getPRDMultiply().getArg2(), propertyDefinition);
            action = new ActionMultiply(actionType, entityName, resultPropertyName, arg1, arg2);
        } else if (prdObject.getPRDDivide() != null) {
            Expression arg1 = getExpression(prdObject.getPRDDivide().getArg1(), propertyDefinition);
            Expression arg2 = getExpression(prdObject.getPRDDivide().getArg2(), propertyDefinition);
            action = new ActionDivide(actionType, entityName, resultPropertyName, arg1, arg2);
        } else {
            throw new IllegalArgumentException("CalculationAction has no multiply or divide objects");
        }

        return action;
    }

    public ActionSet getActionSet(PRDAction prdObject) throws InvalidClassException {
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getValue(), primaryEntityDefinition.getProperties().get(propertyName));
        return new ActionSet(actionType, entityName, propertyName, value);
    }

    public Expression getExpression(String expressionString, PropertyDefinition propertyDefinition) throws InvalidClassException {
        String[] words = expressionString.split("\\(");
        String firstWord = words[0];
        String secondWord = words[1].substring(0, words[1].length() - 1);

        try {
            FunctionType type = FunctionType.valueOf(firstWord.toUpperCase());
            // Check and handle auxiliary function expression
            switch (type) {
                case ENVIRONMENT:
                    PropertyInstance envProperty = activeEnvironment.getProperty(secondWord);
                    if(!Validator
                            .validate(envProperty.getPropertyDefinition().getType().toString())
                            .isCompatibleWith(propertyDefinition.getType(), secondWord)
                            .isValid()){
                        throw new InvalidClassException("Properties not of same type: " + envProperty.getPropertyDefinition().getType() + " - " + propertyDefinition.getType());
                    }
                    return new EnvironmentExpression(envProperty);
                case RANDOM:
                    int range;
                    try {
                        range = Integer.parseInt(secondWord);
                    }
                    catch (NumberFormatException e){
                        throw new UnsupportedOperationException("The auxiliary function Random must get only integer values.");
                    }
                    return new RandomExpression(range);
                default:
                    throw new UnsupportedOperationException("Function type not supported");
            }
        } catch (IllegalArgumentException e) {
            if (primaryEntityDefinition.getProperties().values().stream().anyMatch(
                    property -> property.getName().equals(expressionString))) {
                if(!Validator
                        .validate(primaryEntityDefinition.getProperties().get(expressionString).getType().toString())
                        .isCompatibleWith(propertyDefinition.getType(), expressionString)
                        .isValid()){
                    throw new InvalidClassException("Properties not of same type: " + primaryEntityDefinition.getProperties().get(expressionString).getType() + " - " + propertyDefinition.getType());
                }
                return new EntityPropertyExpression(expressionString);
            } else {
                PropertyType type = propertyDefinition.getType();
                switch (type){
                    case DECIMAL:
                        return new FreeValueExpression(Integer.parseInt(expressionString));
                    case BOOLEAN:
                        return new FreeValueExpression(Boolean.parseBoolean(expressionString));
                    case FLOAT:
                        return new FreeValueExpression(Double.parseDouble(expressionString));
                    case STRING:
                        if(Validator.validate(expressionString).isValidString().isValid()){
                            return new FreeValueExpression(expressionString);
                        }
                        break;
                }
                throw new IllegalArgumentException("Property type and free value expression type don't match");
            }
        }
    }

    public Action getAction(PRDAction prdObject) throws InvalidClassException {
        ActionType type = ActionType.valueOf(prdObject.getType());
        String entityName = prdObject.getEntity();
        if(!primaryEntityDefinition.getName().equals(entityName)){
            throw new IllegalArgumentException("Entity '" + entityName + "' referenced in '" + type + "' action does not exist.");
        }
        if(!type.toString().equals("condition") && !type.toString().equals("kill")) {
            PropertyDefinition propertyDefinition = primaryEntityDefinition.getProperties().get(prdObject.getProperty());
            if (propertyDefinition == null) {
                throw new IllegalArgumentException("Property '" + prdObject.getProperty() + "' referenced in '" + type + "' action does not exist.");
            }
        }
        Action action = null;
        switch (type) {
            case calculation:
                action = getActionCalc(prdObject);
                break;
            case condition:
                action = getActionCondition(prdObject);
                break;
            case decrease:
                action = getActionDecrease(prdObject);
                break;
            case increase:
                action = getActionIncrease(prdObject);
                break;
            case kill:
                action = getActionKill(prdObject);
                break;
            case set:
                action = getActionSet(prdObject);
                break;
        }
        return action;
    }

    public EntityDefinition getEntityDefinition(PRDEntity prdObject){
        String name = prdObject.getName();
        int population = prdObject.getPRDPopulation();
        Map<String, PropertyDefinition> properties = new HashMap<>();
        for (PRDProperty prdProperty : prdObject.getPRDProperties().getPRDProperty()) {
            PropertyDefinition propertyDefinition = getPropertyDefinition(prdProperty);
            String propertyName = propertyDefinition.getName();
            if(properties.get(propertyName) != null){
                throw new IllegalArgumentException("Duplicate property name '" + propertyName + "' within entity '" + name + "'.");
            }
            properties.put(propertyDefinition.getName(), propertyDefinition);
        }
        return new EntityDefinition(name, population, properties);
    }

    public EnvironmentManager getEnvironmentManager(PRDEvironment prdObject){
        Map<String, PropertyDefinition> properties = new HashMap<>();
        for (PRDEnvProperty prdProperty : prdObject.getPRDEnvProperty()) {
            String name =prdProperty.getPRDName();
            if(properties.get(name) != null){
                throw new IllegalArgumentException("Duplicate environment variable name '" + name + "'.");
            }
            PropertyType type = PropertyType.valueOf(prdProperty.getType().toUpperCase());
            Range range = getRange(prdProperty.getPRDRange());
            PropertyDefinition propertyDefinition = getPropertyDefinitionByType(name, type, null, range, true);
            properties.put(prdProperty.getPRDName(), propertyDefinition);
        }
        return new EnvironmentManager(properties);
    }

    public Range getRange(@NotNull PRDRange prdObject){
        return new Range(prdObject.getFrom(), prdObject.getTo());
    }

    public PropertyDefinition getPropertyDefinition(PRDProperty prdObject){
        String propertyName = prdObject.getPRDName();
        PropertyType type = PropertyType.valueOf(prdObject.getType().toUpperCase());
        String init = prdObject.getPRDValue().getInit();
        Range range = getRange(prdObject.getPRDRange());
        boolean isRandomInit = prdObject.getPRDValue().isRandomInitialize();

        return getPropertyDefinitionByType(propertyName, type, init, range, isRandomInit);
    }

    public PropertyDefinition getPropertyDefinitionByType(String name, PropertyType type, String init, Range range, boolean isRandomInit){
        PropertyDefinition propertyDefinition;
        switch (type) {
            case DECIMAL: {
                ValueGenerator<Integer> valueGenerator;
                if (!isRandomInit) {
                    int value = Integer.parseInt(init);
                    valueGenerator = ValueGeneratorFactory.createFixed(value);
                } else {
                    valueGenerator = ValueGeneratorFactory.createRandomInteger(range);
                }
                propertyDefinition = new IntegerPropertyDefinition(name, range, valueGenerator);
                break;
            }
            case BOOLEAN: {
                propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createRandomBoolean());
                break;
            }
            case FLOAT: {
                ValueGenerator<Double> valueGenerator;
                if (!isRandomInit) {
                    double value = Double.parseDouble(init);
                    valueGenerator = ValueGeneratorFactory.createFixed(value);
                } else {
                    valueGenerator = ValueGeneratorFactory.createRandomDouble(range);
                }
                propertyDefinition = new DoublePropertyDefinition(name, range, valueGenerator);
                break;
            }
            case STRING: {
                propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createRandomString());
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid property type");
        }
        return propertyDefinition;
    }

    public Termination getTermination(PRDTermination prdObject){
        ByTicks byTicks = null;
        BySecond bySecond = null;
        for (Object terminationCondition : prdObject.getPRDByTicksOrPRDBySecond()) {
            if (terminationCondition.getClass() == PRDByTicks.class){
                byTicks = getByTicks((PRDByTicks) terminationCondition);
            } else if (terminationCondition.getClass() == PRDBySecond.class) {
                bySecond = getBySecond((PRDBySecond) terminationCondition);
            }
        }
        return new Termination(byTicks, bySecond);
    }

    public ByTicks getByTicks(PRDByTicks prdObject){
        return new ByTicks(prdObject.getCount());
    }

    public BySecond getBySecond(PRDBySecond prdObject){
        return new BySecond(prdObject.getCount());
    }
}
