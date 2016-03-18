package demos.math.basic;

import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;

import toberumono.lexer.BasicDescender;
import toberumono.lexer.BasicLexer;
import toberumono.lexer.BasicRule;
import toberumono.lexer.util.DefaultIgnorePatterns;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A demo usage of {@link BasicLexer} that implements the required functions to evaluate an algebraic expression using order
 * of operations.
 * 
 * @author Toberumono
 */
public class MathParser {
	private static final ConsType NUMBER = new ConsType("number"), OPERATOR = new ConsType("operator"), PARENTHESES = new ConsType("parentheses", "(", ")");
	
	private static final Operator addition = new Operator(0, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) + ((Double) u.getCar())), NUMBER));
	private static final Operator subtraction = new Operator(0, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) - ((Double) u.getCar())), NUMBER));
	private static final Operator multiplication = new Operator(1, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) * ((Double) u.getCar())), NUMBER));
	private static final Operator division = new Operator(1, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) / ((Double) u.getCar())), NUMBER));
	private static final Operator mod = new Operator(2, (t, u) -> new ConsCell(new Double(((Double) t.getCar()) % ((Double) u.getCar())), NUMBER));
	private static final Operator power = new Operator(3, (t, u) -> new ConsCell(new Double(Math.pow((Double) t.getCar(), (Double) u.getCar())), NUMBER));
	
	private final BasicLexer lexer;
	
	/**
	 * Initializes a {@link MathParser} that handles algebraic expressions.
	 */
	public MathParser() {
		lexer = new BasicLexer(DefaultIgnorePatterns.WHITESPACE); //We don't want to error out if there is whitespace in the expression
		lexer.addDescender("parentheses", new BasicDescender("(", ")", PARENTHESES));
		lexer.addRule("number", new BasicRule(Pattern.compile("([0-9]+(\\.[0-9]*)?|\\.[0-9]+)"), (l, s, m) -> new ConsCell(Double.parseDouble(m.group()), NUMBER)));
		lexer.addRule("addition", new BasicRule(Pattern.compile("+", Pattern.LITERAL), (l, s, m) -> new ConsCell(addition, OPERATOR)));
		lexer.addRule("subtraction", new BasicRule(Pattern.compile("-", Pattern.LITERAL), (l, s, m) -> new ConsCell(subtraction, OPERATOR)));
		lexer.addRule("multiplication", new BasicRule(Pattern.compile("*", Pattern.LITERAL), (l, s, m) -> new ConsCell(multiplication, OPERATOR)));
		lexer.addRule("division", new BasicRule(Pattern.compile("/", Pattern.LITERAL), (l, s, m) -> new ConsCell(division, OPERATOR)));
		lexer.addRule("mod", new BasicRule(Pattern.compile("%", Pattern.LITERAL), (l, s, m) -> new ConsCell(mod, OPERATOR)));
		lexer.addRule("power", new BasicRule(Pattern.compile("^", Pattern.LITERAL), (l, s, m) -> new ConsCell(power, OPERATOR)));
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
		return (Double) evaluateExpression(lexer.lex(expression)).getCar();
	}
	
	/**
	 * Evaluates the algebraic expression encoded in the given {@link ConsCell}.<br>
	 * <b>NOTE:</b> This directly modifies {@code expression}. Make a copy of {@code expression} if you will need to re-use
	 * it.
	 * 
	 * @param expression
	 *            a {@link ConsCell} containing an algebraic expression
	 * @return a {@link ConsCell} containing the result of evaluating the expression as a {@link Double}
	 * @see #evaluateExpression(String)
	 */
	public ConsCell evaluateExpression(ConsCell expression) {
		ConsCell current, left, right;
		for (int i = 3; i >= 0; i--)
			for (current = expression; current != null; current = current.getNextConsCell())
				if (current.getCarType() == OPERATOR && ((Operator) current.getCar()).getPriority() == i) {
					left = handleParentheses(current.getPreviousConsCell());
					right = handleParentheses(current.getNextConsCell());
					
					current.replaceCar(((Operator) current.getCar()).apply(left, right));
					
					if (left == expression)
						expression = current;
					left.remove(); //We're done processing both the left and right expressions
					right.remove();
				}
		return expression;
	}
	
	/**
	 * Replaces the {@code car} value of the given {@link ConsCell} with the result of calling
	 * {@link #evaluateExpression(ConsCell)} on its {@code car} value if it is a parenthetical expression.
	 * 
	 * @param expression
	 *            a {@link ConsCell} that can contain a parenthetical expression
	 * @return {@code expression}, possibly with its {@code car} value replaced
	 */
	protected ConsCell handleParentheses(ConsCell expression) {
		if (expression.getCarType() == PARENTHESES)
			expression.replaceCar(evaluateExpression((ConsCell) expression.getCar()));
		return expression;
	}
	
	/**
	 * A simple REPL for the {@link MathParser}.
	 * 
	 * @param args
	 *            command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		MathParser parser = new MathParser();
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

class Operator implements BinaryOperator<ConsCell> {
	private final int priority;
	private final BinaryOperator<ConsCell> operation;
	
	public Operator(int priority, BinaryOperator<ConsCell> operation) {
		this.priority = priority;
		this.operation = operation;
	}
	
	public int getPriority() {
		return priority;
	}
	
	@Override
	public ConsCell apply(ConsCell t, ConsCell u) {
		return operation.apply(t, u);
	}
}
