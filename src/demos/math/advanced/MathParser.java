package demos.math.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import toberumono.lexer.BasicDescender;
import toberumono.lexer.BasicLexer;
import toberumono.lexer.BasicRule;
import toberumono.lexer.errors.LexerException;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A demo usage of {@link BasicLexer} that implements the required functions to evaluate an algebraic expression with
 * user-defined operators.
 * 
 * @author Toberumono
 */
public class MathParser {
	private static final ConsType NUMBER = new ConsType("number"), OPERATOR = new ConsType("operator");
	private static final Operator addition = new Operator(0, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) + ((Double) u.getCar())), NUMBER));
	private static final Operator subtraction = new Operator(0, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) - ((Double) u.getCar())), NUMBER));
	private static final Operator multiplication = new Operator(1, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) * ((Double) u.getCar())), NUMBER));
	private static final Operator division = new Operator(1, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) / ((Double) u.getCar())), NUMBER));
	private static final Operator mod = new Operator(2, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) % ((Double) u.getCar())), NUMBER));
	private static final Operator power = new Operator(3, (t, u) -> new ConsCell(new Double(Math.pow((Double) t.getCar(), (Double) u.getCar())), NUMBER));
	
	private final BasicLexer lexer;
	private final Map<String, Operator> operators;
	private int highestPrecedence = 0, lowestPrecedence = 0;

	/**
	 * Initializes a {@link MathParser} that handles algebraic expressions.
	 */
	public MathParser() {
		this(true);
	}
	
	public MathParser(boolean loadCoreOperators) {
		lexer = new BasicLexer(DefaultIgnorePatterns.WHITESPACE);
		operators = new HashMap<>();
		lexer.addDescender("parentheses", new BasicDescender("(", ")", (l, s, m) -> evaluateExpression(m)));
		lexer.addRule("number", new BasicRule(Pattern.compile("([0-9]+(\\.[0-9]*)?|\\.[0-9]+)"), (l, s, m) -> new ConsCell(Double.parseDouble(m.group()), OPERATOR)));
		if (loadCoreOperators) {
			addOperator("addition", Pattern.compile("+", Pattern.LITERAL), addition);
			addOperator("subtraction", Pattern.compile("-", Pattern.LITERAL), subtraction);
			addOperator("multiplication", Pattern.compile("*", Pattern.LITERAL), multiplication);
			addOperator("division", Pattern.compile("/", Pattern.LITERAL), division);
			addOperator("mod", Pattern.compile("%", Pattern.LITERAL), mod);
			addOperator("power", Pattern.compile("^", Pattern.LITERAL), power);
		}
	}
	
	public boolean addOperator(String name, Pattern pattern, Operator operator) {
		if (name == null)
			throw new NullPointerException("The operator's name cannot be null");
		if (pattern == null)
			throw new NullPointerException("The operator's identifying pattern cannot be null");
		if (operator == null)
			throw new NullPointerException("The operator itself cannot be null");
		if (operators.containsKey(name))
			return false;
		lexer.addRule(name, new BasicRule(pattern, (l, s, m) -> new ConsCell(operator, OPERATOR)));
		if (operator.getPrecendence() > highestPrecedence)
			highestPrecedence = operator.getPrecendence();
		else if (operator.getPrecendence() < lowestPrecedence)
			lowestPrecedence = operator.getPrecendence();
		operators.put(name, operator);
		return true;
	}
	
	public Operator removeOperator(String name) {
		if (!operators.containsKey(name))
			return null;
		lexer.removeRule(name);
		Operator operator = operators.remove(name); //Guaranteed to not be null
		if (operator.getPrecendence() == highestPrecedence || operator.getPrecendence() == lowestPrecedence) {
			highestPrecedence = 0;
			lowestPrecedence = 0;
			for (Operator op : operators.values()) {
				if (op.getPrecendence() > highestPrecedence)
					highestPrecedence = op.getPrecendence();
				else if (op.getPrecendence() < lowestPrecedence)
					lowestPrecedence = op.getPrecendence();
			}
		}
		return operator;
	}
	
	public Double evaluateExpression(String expression) throws LexerException {
		return ((Number) evaluateExpression(lexer.lex(expression)).getCar()).doubleValue();
	}
	
	public ConsCell evaluateExpression(ConsCell expression) {
		ConsCell current, left, right;
		for (int i = highestPrecedence; i >= lowestPrecedence; i--) {
			current = expression;
			while (!current.isLastConsCell()) {
				if (current.getCarType() != OPERATOR)
					current = current.getNextConsCell();
				else {
					if (((Operator) current.getCar()).getPrecendence() == i) {
						left = current.getPreviousConsCell();
						right = current.getNextConsCell();
						
						current.replaceCar(((Operator) current.getCar()).apply(left, right));
						
						if (left == expression)
							expression = current;
						left.remove();
						right.remove();
						current = current.getNextConsCell();
					}
				}
			}
		}
		return expression;
	}
}
