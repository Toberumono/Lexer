package demos.math.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import toberumono.lexer.BasicDescender;
import toberumono.lexer.BasicLexer;
import toberumono.lexer.BasicRule;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.BasicConsType;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A demo usage of {@link BasicLexer} that implements the required functions to evaluate an algebraic expression with
 * user-defined operators.
 * 
 * @author Toberumono
 */
public class ExtendableMathParser {
	private static final ConsType NUMBER = new BasicConsType("number"), OPERATOR = new BasicConsType("operator");
	private static final Operator addition = new Operator(0, (t, u) -> new ConsCell(new Double(((Number) t.getCar()).doubleValue() + ((Number) u.getCar()).doubleValue()), NUMBER));
	private static final Operator subtraction = new Operator(0, (t, u) -> new ConsCell(new Double(((Number) t.getCar()).doubleValue() - ((Number) u.getCar()).doubleValue()), NUMBER));
	private static final Operator multiplication = new Operator(1, (t, u) -> new ConsCell(new Double(((Number) t.getCar()).doubleValue() * ((Number) u.getCar()).doubleValue()), NUMBER));
	private static final Operator division = new Operator(1, (t, u) -> new ConsCell(new Double(((Number) t.getCar()).doubleValue() / ((Number) u.getCar()).doubleValue()), NUMBER));
	private static final Operator mod = new Operator(2, (t, u) -> new ConsCell(new Double(((Number) t.getCar()).doubleValue() % ((Number) u.getCar()).doubleValue()), NUMBER));
	private static final Operator power = new Operator(3, (t, u) -> new ConsCell(new Double(Math.pow(((Number) t.getCar()).doubleValue(), ((Number) u.getCar()).doubleValue())), NUMBER));
	
	private final BasicLexer lexer;
	
	private final Map<String, Operator> operators;
	private int highestPrecedence = 0, lowestPrecedence = 0;
	
	/**
	 * Initializes a {@link ExtendableMathParser} that handles algebraic expressions.
	 */
	public ExtendableMathParser() {
		this(true);
	}
	
	/**
	 * Initializes a {@link ExtendableMathParser} that optionally loads addition, subtraction, multiplication, division,
	 * modulo, and power operators if {@code loadCoreOperators} is set to {@code true}.
	 * 
	 * @param loadCoreOperators
	 *            whether to load the basic mathematical operators as previously described
	 */
	public ExtendableMathParser(boolean loadCoreOperators) {
		lexer = new BasicLexer(DefaultIgnorePatterns.WHITESPACE);
		operators = new HashMap<>();
		lexer.addDescender("parentheses", new BasicDescender("(", ")", (l, s, m) -> evaluateExpression(m)));
		lexer.addRule("number", new BasicRule(Pattern.compile("([0-9]+(\\.[0-9]*)?|\\.[0-9]+)"), (l, s, m) -> new ConsCell(Double.parseDouble(m.group()), NUMBER)));
		if (loadCoreOperators) {
			addOperator("addition", Pattern.compile("+", Pattern.LITERAL), addition);
			addOperator("subtraction", Pattern.compile("-", Pattern.LITERAL), subtraction);
			addOperator("multiplication", Pattern.compile("*", Pattern.LITERAL), multiplication);
			addOperator("division", Pattern.compile("/", Pattern.LITERAL), division);
			addOperator("mod", Pattern.compile("%", Pattern.LITERAL), mod);
			addOperator("power", Pattern.compile("^", Pattern.LITERAL), power);
		}
	}
	
	/**
	 * Adds an {@link Operator} with the given {@code name} and {@link Pattern} to the {@link ExtendableMathParser}.
	 * 
	 * @param name
	 *            the name of the {@link Operator}
	 * @param pattern
	 *            the {@link Pattern} by which the operator can be identified
	 * @param operator
	 *            the {@link Operator} to add
	 * @return {@code true} if the {@link Operator} was successfully added
	 */
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
		if (operator.getPrecedence() > highestPrecedence)
			highestPrecedence = operator.getPrecedence();
		else if (operator.getPrecedence() < lowestPrecedence)
			lowestPrecedence = operator.getPrecedence();
		operators.put(name, operator);
		return true;
	}
	
	/**
	 * Removes an {@link Operator} from the {@link ExtendableMathParser} by name if it exists.
	 * 
	 * @param name
	 *            the name of the {@link Operator} to remove
	 * @return the removed {@link Operator} or {@code null} if the named {@link Operator} was not found in the
	 *         {@link ExtendableMathParser}
	 */
	public Operator removeOperator(String name) {
		if (!operators.containsKey(name))
			return null;
		lexer.removeRule(name);
		Operator operator = operators.remove(name); //Guaranteed to not be null
		if (operator.getPrecedence() == highestPrecedence || operator.getPrecedence() == lowestPrecedence) {
			highestPrecedence = 0;
			lowestPrecedence = 0;
			for (Operator op : operators.values()) {
				if (op.getPrecedence() > highestPrecedence)
					highestPrecedence = op.getPrecedence();
				else if (op.getPrecedence() < lowestPrecedence)
					lowestPrecedence = op.getPrecedence();
			}
		}
		return operator;
	}
	
	/**
	 * Evaluates the algebraic expression encoded in the given {@link String}.<br>
	 * Forwards to {@link #evaluateExpression(ConsCell)}
	 * 
	 * @param expression
	 *            a {@link String} containing an algebraic expression
	 * @return a {@link Double} equal to the result of evaluating the expression
	 * @see #evaluateExpression(ConsCell)
	 */
	public Double evaluateExpression(String expression) {
		return ((Number) evaluateExpression(lexer.lex(expression)).getCar()).doubleValue();
	}
	
	/**
	 * Evaluates the algebraic expression encoded in the given {@link ConsCell}.<br>
	 * <b>NOTE:</b> This directly modifies {@code expression}. Make a copy of {@code expression} if you will need to reuse
	 * it.
	 * 
	 * @param expression
	 *            a {@link ConsCell} containing an algebraic expression
	 * @return a {@link ConsCell} containing the result of evaluating the expression as a {@link Double}
	 * @see #evaluateExpression(String)
	 */
	public ConsCell evaluateExpression(ConsCell expression) {
		ConsCell current, left, right;
		for (int i = highestPrecedence; i >= lowestPrecedence; i--)
			for (current = expression; current != null; current = current.getNext())
				if (current.getCarType() == OPERATOR && ((Operator) current.getCar()).getPrecedence() == i) {
					left = current.getPrevious();
					right = current.getNext();
					
					current.replaceCar(((Operator) current.getCar()).apply(left, right));
					
					if (left == expression)
						expression = current;
					left.remove(); //We're done processing both the left and right expressions
					right.remove();
				}
		return expression;
	}
	
	/**
	 * A simple REPL for the {@link ExtendableMathParser}.
	 * 
	 * @param args
	 *            command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		ExtendableMathParser parser = new ExtendableMathParser();
		System.out.println("Enter equations on single lines. The program exits on EOF or an empty line.");
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				if (input.length() == 0)
					return;
				System.out.println(input + " = " + parser.evaluateExpression(input));
			}
		}
	}
}
