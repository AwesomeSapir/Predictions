package translation.xml;

import com.sun.istack.internal.NotNull;
import engine.prd.*;
import world.World;
import world.definition.entity.EntityDefinition;
import world.definition.property.*;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.instance.environment.EnvironmentManager;
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
import world.termination.TerminationCondition;
import world.type.Range;
import world.value.generator.ValueGenerator;
import world.value.generator.ValueGeneratorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTranslator implements Translator{

    private final PRDWorld prdWorld;
    private World world;
    private EnvironmentManager environmentManager;
    private EntityDefinition primaryEntityDefinition;

    private Termination termination;

    public XmlTranslator(PRDWorld prdWorld) {
        this.prdWorld = prdWorld;
    }

    @Override
    public World getWorld() {
        environmentManager = getEnvironmentManager(prdWorld.getPRDEvironment());

        primaryEntityDefinition = getEntityDefinition(prdWorld.getPRDEntities().getPRDEntity().get(0));

        termination = getTermination(prdWorld.getPRDTermination());

        Map<String, Rule> rules = new HashMap<>();
        for (PRDRule rule : prdWorld.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), getRule(rule));
        }

        return new World(environmentManager, primaryEntityDefinition, rules, termination);
    }

    public Rule getRule(PRDRule prdObject){
        List<Action> actions = new ArrayList<>();
        String name = prdObject.getName();
        Activation activation = new Activation(prdObject.getPRDActivation());
        for (PRDAction prdAction : prdObject.getPRDActions().getPRDAction()) {
            actions.add(getAction(prdAction));
        }
        return new Rule(name, activation, actions);
    }

    public ActionCondition getActionCondition(PRDAction prdObject){
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

    public MultiCondition getMultiCondition(PRDCondition prdObject){
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

    public SingleCondition getSingleCondition(PRDCondition prdObject){
        Operator operator = Operator.fromDRP(prdObject.getOperator());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getValue());
        return new SingleCondition(operator, propertyName, value);
    }

    public ActionIncrease getActionIncrease(PRDAction prdObject){
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy());
        return new ActionIncrease(actionType, entityName, propertyName, value);
    }

    public ActionDecrease getActionDecrease(PRDAction prdObject){
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getBy());
        return new ActionDecrease(actionType, entityName, propertyName, value);
    }

    public ActionKill getActionKill(PRDAction prdObject){
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        return new ActionKill(actionType, entityName);
    }

    public ActionCalc getActionCalc(PRDAction prdObject){
        ActionCalc action;
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String resultPropertyName = prdObject.getProperty();

        if (prdObject.getPRDMultiply() != null) {
            Expression arg1 = getExpression(prdObject.getPRDMultiply().getArg1());
            Expression arg2 = getExpression(prdObject.getPRDMultiply().getArg2());
            action = new ActionMultiply(actionType, entityName, resultPropertyName, arg1, arg2);
        } else if (prdObject.getPRDDivide() != null) {
            Expression arg1 = getExpression(prdObject.getPRDDivide().getArg1());
            Expression arg2 = getExpression(prdObject.getPRDDivide().getArg2());
            action = new ActionDivide(actionType, entityName, resultPropertyName, arg1, arg2);
        } else {
            throw new IllegalArgumentException("CalculationAction has no multiply or divide objects");
        }

        return action;
    }

    public ActionSet getActionSet(PRDAction prdObject){
        String entityName = prdObject.getEntity();
        ActionType actionType = ActionType.valueOf(prdObject.getType());
        String propertyName = prdObject.getProperty();
        Expression value = getExpression(prdObject.getValue());
        return new ActionSet(actionType, entityName, propertyName, value);
    }

    public Expression getExpression(String exp){

    }

    public Action getAction(PRDAction prdObject){
        ActionType type = ActionType.valueOf(prdObject.getType());
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
            properties.put(propertyDefinition.getName(), propertyDefinition);
        }
        return new EntityDefinition(name, population, properties);
    }

    public EnvironmentManager getEnvironmentManager(PRDEvironment prdObject){
        Map<String, PropertyDefinition> properties = new HashMap<>();
        for (PRDEnvProperty prdProperty : prdObject.getPRDEnvProperty()) {
            PropertyType type = PropertyType.valueOf(prdProperty.getType().toUpperCase());
            Range range = getRange(prdProperty.getPRDRange());
            PropertyDefinition propertyDefinition = getPropertyDefinitionByType(prdProperty.getPRDName(), type, null, range, true);
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
        List<TerminationCondition> terminationConditions = new ArrayList<>();
        for (Object terminationCondition : prdObject.getPRDByTicksOrPRDBySecond()) {
            if (terminationCondition.getClass() == PRDByTicks.class){
                terminationConditions.add(getByTicks((PRDByTicks) terminationCondition));
            } else if (terminationCondition.getClass() == PRDBySecond.class) {
                terminationConditions.add(getBySecond((PRDBySecond) terminationCondition));
            }
        }
        return new Termination(terminationConditions);
    }

    public ByTicks getByTicks(PRDByTicks prdObject){
        return new ByTicks(prdObject.getCount());
    }

    public BySecond getBySecond(PRDBySecond prdObject){
        return new BySecond(prdObject.getCount());
    }
}
