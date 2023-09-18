package translation.xml;

import com.sun.istack.internal.NotNull;
import engine.prd.*;
import engine.simulation.world.ValueType;
import engine.simulation.world.World;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.*;
import engine.simulation.world.expression.*;
import engine.simulation.world.expression.auxiliary.*;
import engine.simulation.world.instance.entity.EntityManager;
import engine.simulation.world.instance.environment.ActiveEnvironment;
import engine.simulation.world.instance.environment.EnvironmentManager;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.rule.Activation;
import engine.simulation.world.rule.Rule;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;
import engine.simulation.world.rule.action.SecondaryEntity;
import engine.simulation.world.rule.action.type.calculation.ActionCalc;
import engine.simulation.world.rule.action.type.calculation.ActionDivide;
import engine.simulation.world.rule.action.type.calculation.ActionMultiply;
import engine.simulation.world.rule.action.type.condition.*;
import engine.simulation.world.rule.action.type.space.ActionKill;
import engine.simulation.world.rule.action.type.space.ActionProximity;
import engine.simulation.world.rule.action.type.space.ActionReplace;
import engine.simulation.world.rule.action.type.space.ReplaceMode;
import engine.simulation.world.rule.action.type.value.ActionDecrease;
import engine.simulation.world.rule.action.type.value.ActionIncrease;
import engine.simulation.world.rule.action.type.value.ActionSet;
import engine.simulation.world.space.SpaceManager;
import engine.simulation.world.termination.BySecond;
import engine.simulation.world.termination.ByTicks;
import engine.simulation.world.termination.ByUser;
import engine.simulation.world.termination.Termination;
import engine.simulation.world.type.Range;
import engine.simulation.world.value.generator.ValueGenerator;
import engine.simulation.world.value.generator.ValueGeneratorFactory;
import validator.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.util.*;

import static engine.simulation.world.expression.ExpressionType.FREE_VALUE;

public class XmlTranslator implements Translator {

    private final PRDWorld prdWorld;
    private World world;
    private EnvironmentManager environmentManager;
    private ActiveEnvironment activeEnvironment;
    private EntityManager entityManager;
    private Termination termination;

    private static final String JAXB_XML_GAME_PACKAGE_NAME = "engine.prd";

    public XmlTranslator(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        this.prdWorld = (PRDWorld) u.unmarshal(in);
    }

    @Override
    public World getWorld() throws InvalidClassException {
        SpaceManager spaceManager = getSpace(prdWorld.getPRDGrid());
        environmentManager = getEnvironmentManager(prdWorld.getPRDEnvironment());

        activeEnvironment = environmentManager.createActiveEnvironment();
        activeEnvironment.initProperties(environmentManager.getVariables());

        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        for (PRDEntity prdEntity : prdWorld.getPRDEntities().getPRDEntity()) {
            entityDefinitions.add(getEntityDefinition(prdEntity));
        }
        entityManager = new EntityManager(entityDefinitions);

        termination = getTermination(prdWorld.getPRDTermination());

        Map<String, Rule> rules = new LinkedHashMap<>();
        for (PRDRule rule : prdWorld.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), getRule(rule));
        }

        world = new World(environmentManager, activeEnvironment, entityManager, rules, termination, spaceManager, prdWorld.getPRDThreadCount());
        return world;
    }

    public SpaceManager getSpace(PRDWorld.PRDGrid prdObject) {
        int rows = prdObject.getRows();
        int cols = prdObject.getColumns();
        if (!Validator.validate(String.valueOf(rows)).isInRange(10, 100).isValid()) {
            throw new IllegalArgumentException("Rows must be between 10-100.");
        }
        if (!Validator.validate(String.valueOf(cols)).isInRange(10, 100).isValid()) {
            throw new IllegalArgumentException("Columns must be between 10-100.");
        }
        return new SpaceManager(rows, cols);
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

    public Activation getActivation(PRDActivation prdObject) {
        Activation activation;
        if (prdObject == null) {
            activation = new Activation(null, null);
        } else {
            Integer ticks = prdObject.getTicks();
            Double probability = prdObject.getProbability();
            activation = new Activation(ticks, probability);
        }
        return activation;
    }

    public ActionCondition getActionCondition(PRDAction prdObject, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) throws InvalidClassException {
        MultiCondition conditions = getMultiCondition(prdObject.getPRDCondition());
        List<Action> actionsThen = new ArrayList<>();
        List<Action> actionsElse = new ArrayList<>();
        for (PRDAction prdThenAction : prdObject.getPRDThen().getPRDAction()) {
            actionsThen.add(getAction(prdThenAction));
        }
        if (prdObject.getPRDElse() != null) {
            for (PRDAction prdElseAction : prdObject.getPRDElse().getPRDAction()) {
                actionsElse.add(getAction(prdElseAction));
            }
        }
        return new ActionCondition(primaryEntity, secondaryEntity, conditions, actionsThen, actionsElse);
    }

    public MultiCondition getMultiCondition(PRDCondition prdObject) throws InvalidClassException {
        Logical logical;
        List<Condition> subConditions = new ArrayList<>();
        Singularity singularity = Singularity.valueOf(prdObject.getSingularity());
        if (singularity == Singularity.multiple) {
            logical = Logical.valueOf(prdObject.getLogical());
            for (PRDCondition prdCondition : prdObject.getPRDCondition()) {
                switch (Singularity.valueOf(prdCondition.getSingularity())) {
                    case single:
                        subConditions.add(getSingleCondition(prdCondition));
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
        String expressionString = prdObject.getProperty();
        EntityDefinition entity = entityManager.getEntityDefinition(prdObject.getEntity());

        Expression expression = getExpression(expressionString, entity, ValueType.STRING, false);
        Expression value = getExpression(prdObject.getValue(), entity, expression.getValueType());

        return new SingleCondition(operator, expression, value);
    }

    public ActionIncrease getActionIncrease(PRDAction prdObject, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) throws InvalidClassException {
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy(), primaryEntity, primaryEntity.getProperties().get(propertyName).getType());
        return new ActionIncrease(primaryEntity, secondaryEntity, propertyName, value);
    }

    public ActionDecrease getActionDecrease(PRDAction prdObject, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) throws InvalidClassException {
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy(), primaryEntity, primaryEntity.getProperties().get(propertyName).getType());
        return new ActionDecrease(primaryEntity, secondaryEntity, propertyName, value);
    }

    public ActionKill getActionKill(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) {
        return new ActionKill(primaryEntity, secondaryEntity);
    }

    public ActionCalc getActionCalc(PRDAction prdObject, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) throws InvalidClassException {
        ActionCalc action;
        String resultPropertyName = prdObject.getResultProp();
        PropertyDefinition propertyDefinition = primaryEntity.getProperties().get(resultPropertyName);
        ValueType type = propertyDefinition.getType();

        if (prdObject.getPRDMultiply() != null) {
            Expression arg1 = getExpression(prdObject.getPRDMultiply().getArg1(), primaryEntity, type);
            Expression arg2 = getExpression(prdObject.getPRDMultiply().getArg2(), primaryEntity, type);
            action = new ActionMultiply(primaryEntity, secondaryEntity, resultPropertyName, arg1, arg2);
        } else if (prdObject.getPRDDivide() != null) {
            Expression arg1 = getExpression(prdObject.getPRDDivide().getArg1(), primaryEntity, type);
            Expression arg2 = getExpression(prdObject.getPRDDivide().getArg2(), primaryEntity, type);
            action = new ActionDivide(primaryEntity, secondaryEntity, resultPropertyName, arg1, arg2);
        } else {
            throw new IllegalArgumentException("CalculationAction has no multiply or divide objects");
        }

        return action;
    }

    public ActionSet getActionSet(PRDAction prdObject, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) throws InvalidClassException {
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getValue(), primaryEntity, primaryEntity.getProperties().get(propertyName).getType());
        return new ActionSet(primaryEntity, secondaryEntity, propertyName, value);
    }

    public ExpressionType getExpressionType(String expression, EntityDefinition entityDefinition) {
        RawExpression rawExpression = convertToRawExpression(expression);
        if (!rawExpression.getSubexpressions().isEmpty()) { // function
            return ExpressionType.AUXILIARY_FUNCTION;
        } else {
            if (entityDefinition.getProperties().containsKey(expression)) {
                return ExpressionType.ENTITY_PROPERTY;
            }
        }
        return FREE_VALUE;
    }

    public Expression getExpression(String expressionString, EntityDefinition entityDefinition, ValueType valueType) throws InvalidClassException {
        return getExpression(expressionString, entityDefinition, valueType, true);
    }

    public RawExpression convertToRawExpression(String expression){
        RawExpression result;
        if(expression.charAt(expression.length()-1) == ')'){
            int index = expression.indexOf('(');
            result = new RawExpression(expression.substring(0, index));
            String substring = expression.substring(index + 1, expression.length() - 1);
            if(result.getValue().equals("percent")) {
                String[] subexpressions = substring.split(",");
                for (String sub : subexpressions) {
                    result.addExpression(sub);
                }
            } else {
                result.addExpression(substring);
            }
        } else {
            result = new RawExpression(expression);
        }
        return result;
    }

    public Expression getExpression(String expressionString, EntityDefinition entityDefinition, ValueType valueType, boolean compatibilityCheck) throws InvalidClassException {
        ExpressionType type = getExpressionType(expressionString, entityDefinition);
        switch (type) {
            case AUXILIARY_FUNCTION: {
                RawExpression rawExpression = convertToRawExpression(expressionString);
                FunctionType functionType = FunctionType.valueOf(rawExpression.getValue().toUpperCase());
                String arg = rawExpression.getSubexpressions().get(0);
                String[] subwords;
                switch (functionType) {
                    case ENVIRONMENT:
                        PropertyInstance envProperty = activeEnvironment.getProperty(arg);
                        if (compatibilityCheck && !Validator
                                .validate(envProperty.getPropertyDefinition().getType().toString())
                                .isCompatibleWith(valueType, arg)
                                .isValid()) {
                            throw new InvalidClassException("Properties not of same type: " + envProperty.getPropertyDefinition().getType() + " - " + valueType);
                        }
                        return new EnvironmentExpression(envProperty);
                    case RANDOM:
                        int range;
                        try {
                            range = Integer.parseInt(arg);
                        } catch (NumberFormatException e) {
                            throw new UnsupportedOperationException("The auxiliary function Random must get only integer values.");
                        }
                        return new RandomExpression(range);
                    case EVALUATE: {
                        subwords = arg.split("\\.");
                        if (subwords.length != 2) {
                            throw new InvalidClassException("Invalid structure of arguments for function Evaluate");
                        }
                        String entityName = subwords[0];
                        String propertyName = subwords[1];

                        if (entityManager.containsEntityDefinition(entityName)) {
                            if (entityManager.getEntityDefinition(entityName).getProperties().containsKey(propertyName)) {
                                return new EvaluateExpression(entityManager.getEntityDefinition(entityName), entityManager.getEntityDefinition(entityName).getProperties().get(propertyName));
                            }
                            throw new InvalidClassException("Property " + propertyName + " for " + entityName + " in function Evaluate doesn't exist.");
                        }
                        throw new InvalidClassException("Entity " + entityName + " in function Evaluate doesn't exist.");
                    }
                    case PERCENT: {
                        if (rawExpression.getSubexpressions().size() != 2) {
                            throw new InvalidClassException("Invalid structure of arguments for function Percent");
                        }
                        String argExpression = rawExpression.getSubexpressions().get(0);
                        String percentageExpression = rawExpression.getSubexpressions().get(1);
                        Expression argExp = getExpression(argExpression, entityDefinition, valueType, compatibilityCheck);
                        Expression percentage = getExpression(percentageExpression, entityDefinition, valueType, compatibilityCheck);
                        return new PercentExpression(argExp, percentage);
                    }
                    case TICKS: {
                        subwords = arg.split("\\.");
                        if (subwords.length != 2) {
                            throw new InvalidClassException("Invalid structure of arguments for function Ticks");
                        }
                        String entityName = subwords[0];
                        String propertyName = subwords[1];

                        if (entityManager.containsEntityDefinition(entityName)) {
                            if (entityManager.getEntityDefinition(entityName).getProperties().containsKey(propertyName)) {
                                return new TicksExpression(entityManager.getEntityDefinition(entityName), entityManager.getEntityDefinition(entityName).getProperties().get(propertyName));
                            }
                            throw new InvalidClassException("Property " + propertyName + " for " + entityName + " in function Tick doesn't exist.");
                        }
                        throw new InvalidClassException("Entity " + entityName + " in function Tick doesn't exist.");
                    }
                    default:
                        throw new UnsupportedOperationException("Function type not supported");
                }
            }
            case ENTITY_PROPERTY: {
                if (entityDefinition.getProperties().containsKey(expressionString)) {
                    if (compatibilityCheck && !Validator
                            .validate(entityDefinition.getProperties().get(expressionString).getType().toString())
                            .isCompatibleWith(valueType, expressionString)
                            .isValid()) {
                        throw new InvalidClassException("Properties not of same type: " + entityDefinition.getProperties().get(expressionString).getType() + " - " + valueType);
                    }
                    return new EntityPropertyExpression(entityDefinition.getProperties().get(expressionString));
                }
                break;
            }
            case FREE_VALUE: {
                switch (valueType) {
                    case DECIMAL:
                        return new FreeValueExpression(Integer.parseInt(expressionString), valueType);
                    case BOOLEAN:
                        return new FreeValueExpression(Boolean.parseBoolean(expressionString), valueType);
                    case FLOAT:
                        return new FreeValueExpression(Double.parseDouble(expressionString), valueType);
                    case STRING:
                        if (Validator.validate(expressionString).isValidString().isValid()) {
                            return new FreeValueExpression(expressionString, valueType);
                        }
                        break;
                }
                throw new IllegalArgumentException("Property type and free value expression type don't match");
            }
            default:
                throw new InvalidClassException("Fatal error occurred when creating expression: " + expressionString);
        }
        throw new InvalidClassException("Fatal error occurred when creating expression: " + expressionString);
    }

    /*
    public Expression getExpression(String expressionString, EntityDefinition entityDefinition, PropertyDefinition propertyDefinition) throws InvalidClassException {
        try {
            String[] words = expressionString.split("\\(");
            String firstWord = words[0];
            String secondWord = words[1].substring(0, words[1].length() - 1);
            FunctionType type = FunctionType.valueOf(firstWord.toUpperCase());
            String[] subwords;
            // Check and handle auxiliary function expression
            switch (type) {
                case ENVIRONMENT:
                    PropertyInstance envProperty = activeEnvironment.getProperty(secondWord);
                    if (!Validator
                            .validate(envProperty.getPropertyDefinition().getType().toString())
                            .isCompatibleWith(propertyDefinition.getType(), secondWord)
                            .isValid()) {
                        throw new InvalidClassException("Properties not of same type: " + envProperty.getPropertyDefinition().getType() + " - " + propertyDefinition.getType());
                    }
                    return new EnvironmentExpression(envProperty);
                case RANDOM:
                    int range;
                    try {
                        range = Integer.parseInt(secondWord);
                    } catch (NumberFormatException e) {
                        throw new UnsupportedOperationException("The auxiliary function Random must get only integer values.");
                    }
                    return new RandomExpression(range);
                case TICKS: {
                    subwords = secondWord.split("\\.");
                    if (subwords.length != 2) {
                        throw new InvalidClassException("Invalid structure of arguments for function Ticks");
                    }
                    String entityName = subwords[0];
                    String propertyName = subwords[1];

                    if (entityManager.containsEntityDefinition(entityName)) {
                        if (entityManager.getEntityDefinition(entityName).getProperties().containsKey(propertyName)) {
                            return new TicksExpression(entityManager.getEntityDefinition(entityName), entityManager.getEntityDefinition(entityName).getProperties().get(propertyName));
                        }
                        throw new InvalidClassException("Property " + propertyName + " for " + entityName + " in function Tick doesn't exist.");
                    }
                    throw new InvalidClassException("Entity " + entityName + " in function Tick doesn't exist.");
                }
                case EVALUATE: {
                    subwords = secondWord.split("\\.");
                    if (subwords.length != 2) {
                        throw new InvalidClassException("Invalid structure of arguments for function Evaluate");
                    }
                    String entityName = subwords[0];
                    String propertyName = subwords[1];

                    if (entityManager.containsEntityDefinition(entityName)) {
                        if (entityManager.getEntityDefinition(entityName).getProperties().containsKey(propertyName)) {
                            return new EvaluateExpression(entityManager.getEntityDefinition(entityName), entityManager.getEntityDefinition(entityName).getProperties().get(propertyName));
                        }
                        throw new InvalidClassException("Property " + propertyName + " for " + entityName + " in function Evaluate doesn't exist.");
                    }
                    throw new InvalidClassException("Entity " + entityName + " in function Evaluate doesn't exist.");
                }
                case PERCENT:
                    subwords = secondWord.split(",");
                    if (subwords.length != 2) {
                        throw new InvalidClassException("Invalid structure of arguments for function Percent");
                    }
                    String argString = subwords[0];
                    String percentageString = subwords[1];
                    Expression arg = getExpression(argString, entityDefinition, propertyDefinition);
                    Expression percentage = getExpression(percentageString, entityDefinition, propertyDefinition);
                    return new PercentExpression(arg, percentage);
                default:
                    throw new UnsupportedOperationException("Function type not supported");
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            if (entityDefinition.getProperties().values().stream().anyMatch(
                    property -> property.getName().equals(expressionString))) {
                if (!Validator
                        .validate(entityDefinition.getProperties().get(expressionString).getType().toString())
                        .isCompatibleWith(propertyDefinition.getType(), expressionString)
                        .isValid()) {
                    throw new InvalidClassException("Properties not of same type: " + entityDefinition.getProperties().get(expressionString).getType() + " - " + propertyDefinition.getType());
                }
                return new EntityPropertyExpression(entityDefinition.getProperties().get(expressionString));
            } else {
                ValueType type = propertyDefinition.getType();
                switch (type) {
                    case DECIMAL:
                        return new FreeValueExpression(Integer.parseInt(expressionString), ValueType.DECIMAL);
                    case BOOLEAN:
                        return new FreeValueExpression(Boolean.parseBoolean(expressionString), ValueType.BOOLEAN);
                    case FLOAT:
                        return new FreeValueExpression(Double.parseDouble(expressionString), ValueType.FLOAT);
                    case STRING:
                        if (Validator.validate(expressionString).isValidString().isValid()) {
                            return new FreeValueExpression(expressionString, ValueType.STRING);
                        }
                        break;
                }
                throw new IllegalArgumentException("Property type and free value expression type don't match");
            }
        }
    }*/

    public SecondaryEntity getSecondaryEntity(PRDAction.PRDSecondaryEntity prdObject) throws InvalidClassException {
        if (prdObject == null) {
            return null;
        }
        EntityDefinition entityDefinition = entityManager.getEntityDefinition(prdObject.getEntity());
        PRDAction.PRDSecondaryEntity.PRDSelection prdSelection = prdObject.getPRDSelection();
        if (prdSelection.getCount().equals("ALL")) {
            return new SecondaryEntity(entityDefinition);
        } else {
            if (Validator.validate(prdSelection.getCount()).isInteger().isPositive().isValid()) {
                MultiCondition condition;
                if (prdSelection.getPRDCondition() == null) {
                    condition = null;
                } else {
                    condition = getMultiCondition(prdSelection.getPRDCondition());
                }
                return new SecondaryEntity(entityDefinition, Integer.parseInt(prdSelection.getCount()), condition);
            } else {
                throw new IllegalArgumentException("Secondary entity selection for " + entityDefinition.getName() + " must be a positive int: " + prdSelection.getCount());
            }
        }
    }

    public ActionReplace getActionReplace(PRDAction prdObject){
        EntityDefinition killEntity = entityManager.getEntityDefinition(prdObject.getKill());
        EntityDefinition createEntity = entityManager.getEntityDefinition(prdObject.getCreate());
        ReplaceMode mode = ReplaceMode.valueOf(prdObject.getMode().toUpperCase());
        return new ActionReplace(killEntity, createEntity, mode);
    }

    public ActionProximity getActionProximity(PRDAction prdObject) throws InvalidClassException{
        EntityDefinition primaryEntity = entityManager.getEntityDefinition(prdObject.getPRDBetween().getSourceEntity());
        EntityDefinition secondary = entityManager.getEntityDefinition(prdObject.getPRDBetween().getTargetEntity());
        Expression depth = getExpression(prdObject.getPRDEnvDepth().getOf(), primaryEntity, ValueType.FLOAT);
        List<Action> actions = new ArrayList<>();
        for (PRDAction action : prdObject.getPRDActions().getPRDAction()){
            actions.add(getAction(action));
        }
        return new ActionProximity(primaryEntity, secondary, depth, actions);
    }

    public Action getAction(PRDAction prdObject) throws InvalidClassException {
        ActionType type = ActionType.valueOf(prdObject.getType());
        String primaryEntityName = prdObject.getEntity();
        SecondaryEntity secondaryEntity = getSecondaryEntity(prdObject.getPRDSecondaryEntity());
        EntityDefinition primaryEntity = null;
        if (type != ActionType.proximity && type != ActionType.replace) {
            primaryEntity = entityManager.getEntityDefinition(primaryEntityName);

            PropertyDefinition propertyDefinition;
            if (!type.toString().equals("condition") && !type.toString().equals("kill")) {
                if (!type.toString().equals("calculation")) {
                    propertyDefinition = primaryEntity.getProperties().get(prdObject.getProperty());
                } else {
                    propertyDefinition = primaryEntity.getProperties().get(prdObject.getResultProp());
                }
                if (propertyDefinition == null) {
                    throw new IllegalArgumentException("Property '" + prdObject.getProperty() + "' referenced in '" + type + "' action does not exist.");
                }
            }
        }
        Action action = null;
        switch (type) {
            case calculation:
                action = getActionCalc(prdObject, primaryEntity, secondaryEntity);
                break;
            case condition:
                action = getActionCondition(prdObject, primaryEntity, secondaryEntity);
                break;
            case decrease:
                action = getActionDecrease(prdObject, primaryEntity, secondaryEntity);
                break;
            case increase:
                action = getActionIncrease(prdObject, primaryEntity, secondaryEntity);
                break;
            case kill:
                action = getActionKill(primaryEntity, secondaryEntity);
                break;
            case set:
                action = getActionSet(prdObject, primaryEntity, secondaryEntity);
                break;
            case replace:
                action = getActionReplace(prdObject);
                break;
            case proximity:
                action = getActionProximity(prdObject);
                break;
        }
        return action;
    }

    public EntityDefinition getEntityDefinition(PRDEntity prdObject) {
        String name = prdObject.getName();
        Map<String, PropertyDefinition> properties = new HashMap<>();
        for (PRDProperty prdProperty : prdObject.getPRDProperties().getPRDProperty()) {
            PropertyDefinition propertyDefinition = getPropertyDefinition(prdProperty);
            String propertyName = propertyDefinition.getName();
            if (properties.get(propertyName) != null) {
                throw new IllegalArgumentException("Duplicate property name '" + propertyName + "' within entity '" + name + "'.");
            }
            properties.put(propertyDefinition.getName(), propertyDefinition);
        }
        return new EntityDefinition(name, properties);
    }

    public EnvironmentManager getEnvironmentManager(PRDEnvironment prdObject) {
        Map<String, PropertyDefinition> properties = new HashMap<>();
        for (PRDEnvProperty prdProperty : prdObject.getPRDEnvProperty()) {
            String name = prdProperty.getPRDName();
            if (properties.get(name) != null) {
                throw new IllegalArgumentException("Duplicate environment variable name '" + name + "'.");
            }
            ValueType type = ValueType.valueOf(prdProperty.getType().toUpperCase());
            Range range;
            if (prdProperty.getPRDRange() != null) {
                range = getRange(prdProperty.getPRDRange());
            } else {
                range = null;
            }
            PropertyDefinition propertyDefinition = getPropertyDefinitionByType(name, type, null, range, true);
            properties.put(prdProperty.getPRDName(), propertyDefinition);
        }
        return new EnvironmentManager(properties);
    }

    public Range getRange(@NotNull PRDRange prdObject) {
        return new Range(prdObject.getFrom(), prdObject.getTo());
    }

    public PropertyDefinition getPropertyDefinition(PRDProperty prdObject) {
        String propertyName = prdObject.getPRDName();
        ValueType type = ValueType.valueOf(prdObject.getType().toUpperCase());
        String init = prdObject.getPRDValue().getInit();
        Range range;
        if (prdObject.getPRDRange() != null) {
            range = getRange(prdObject.getPRDRange());
        } else {
            range = null;
        }
        boolean isRandomInit = prdObject.getPRDValue().isRandomInitialize();

        return getPropertyDefinitionByType(propertyName, type, init, range, isRandomInit);
    }

    public PropertyDefinition getPropertyDefinitionByType(String name, ValueType type, String init, Range range, boolean isRandomInit) {
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
                propertyDefinition = new IntegerPropertyDefinition(name, range, valueGenerator, isRandomInit);
                break;
            }
            case BOOLEAN: {
                propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createRandomBoolean(), isRandomInit);
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
                propertyDefinition = new DoublePropertyDefinition(name, range, valueGenerator, isRandomInit);
                break;
            }
            case STRING: {
                propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createRandomString(), isRandomInit);
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid property type");
        }
        return propertyDefinition;
    }

    public Termination getTermination(PRDTermination prdObject) {
        ByUser byUser = null;
        ByTicks byTicks = null;
        BySecond bySecond = null;

        if (prdObject.getPRDByUser() != null) {
            byUser = new ByUser();
        }

        for (Object terminationCondition : prdObject.getPRDBySecondOrPRDByTicks()) {
            if (terminationCondition.getClass() == PRDByTicks.class) {
                byTicks = getByTicks((PRDByTicks) terminationCondition);
            } else if (terminationCondition.getClass() == PRDBySecond.class) {
                bySecond = getBySecond((PRDBySecond) terminationCondition);
            }
        }
        if (byUser != null && (byTicks != null || bySecond != null)) {
            throw new IllegalArgumentException("Termination must be configured with either byUser or (byTicks & bySeconds).");
        }
        return new Termination(byUser, byTicks, bySecond);
    }

    public ByTicks getByTicks(PRDByTicks prdObject) {
        return new ByTicks(prdObject.getCount());
    }

    public BySecond getBySecond(PRDBySecond prdObject) {
        return new BySecond(prdObject.getCount());
    }
}
