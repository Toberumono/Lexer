package lexer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;

import lexer.errors.EmptyInputException;
import lexer.errors.LexerException;
import lexer.errors.UnbalancedDescenderException;
import lexer.errors.UnrecognizedCharacterException;

public class Lexer {
	private final PairedList<String, Rule<?>> rules;
	private final PairedList<String, Descender> descenders;
	private final ArrayList<Type<Object>> types;
	private final Stack<DescentPair> descentStack;
	private String input;
	private int head;
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		rules = new PairedList<>();
		descenders = new PairedList<>();
		types = new ArrayList<Type<Object>>();
		descentStack = new Stack<DescentPair>();
		input = "";
		head = 0;
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 */
	public final Token lex(String input) throws LexerException {
		descentStack.push(new DescentPair(this.input, head));
		this.input = input;
		head = 0;
		Token current = new Token(), output = current;
		try {
			while (head < input.length())
				current = current.append(getNextToken(true));
		}
		catch (LexerException e) {
			descentStack.clear();
			throw e;
		}
		this.input = descentStack.peek().getInput();
		head = descentStack.pop().getHead();
		return output;
	}
	
	/**
	 * Gets the next token in the input without stepping this <tt>Lexer</tt> forward.
	 * 
	 * @return the next token in this <tt>Lexer</tt>'s input
	 * @throws LexerException
	 *             if no token was found
	 */
	public final Token getNextToken() throws LexerException {
		return getNextToken(false);
	}
	
	/**
	 * Finds the next token in this <tt>Lexer</tt>
	 * 
	 * @param step
	 *            if this is true, it steps this <tt>Lexer</tt>'s read-head forward
	 * @return the next token in this <tt>Lexer</tt>'s input
	 * @throws LexerException
	 *             if no token was found
	 */
	public final Token getNextToken(boolean step) throws LexerException {
		if (head >= input.length())
			throw new EmptyInputException();
		Descender d = null;
		for (Descender descender : descenders.getValues())
			if (input.length() - head >= descender.open.length() && input.startsWith(descender.open, head) && (d == null || descender.open.length() > d.open.length()))
				d = descender;
		if (d != null) {
			int close = getEndIndex(input, head, d.open, d.close);
			Token result = new Token(d.apply(input.substring(head + d.open.length(), close), this), d.getType());
			if (step) {
				this.head = close + d.close.length();
				while (head < input.length() && input.charAt(head) == ' ')
					head++;
			}
			return result;
		}
		Rule<?> hit = null;
		String match = "";
		Matcher m;
		for (Rule<?> rule : rules.getValues()) {
			m = rule.pattern.matcher(input);
			if (!m.find(head) || m.start() != head || m.group().length() == 0)
				continue;
			hit = rule;
			if (match.length() < m.group().length())
				match = m.group();
		}
		if (hit != null) {
			Token result = new Token(hit.apply(match, this), hit.getType());
			if (step) {
				head += match.length();
				while (head < input.length() && input.charAt(head) == ' ')
					head++;
			}
			return result;
		}
		else
			throw new UnrecognizedCharacterException(input, head);
	}
	
	/**
	 * @return true if there is still untokenized input, otherwise false
	 */
	public final boolean hasNext() {
		return head < input.length();
	}
	
	/**
	 * Adds a new rule
	 * 
	 * @param name
	 *            the name of the rule
	 * @param rule
	 *            the rule
	 */
	public final void addRule(String name, Rule<?> rule) {
		rules.put(name, rule);
	}
	
	/**
	 * Adds a new descender
	 * 
	 * @param name
	 *            the name of the descender
	 * @param descender
	 *            the descender
	 */
	public final void addDescender(String name, Descender descender) {
		descenders.put(name, descender);
	}
	
	/**
	 * @return the types that this lexer can find.
	 */
	public ArrayList<Type<Object>> getTypes() {
		return types;
	}
	
	private int getEndIndex(String input, int start, String startSymbol, String endSymbol) throws UnbalancedDescenderException {
		int index = 0, parenthesis = 0;
		for (int i = start; i < input.length() - startSymbol.length() + 1 && i < input.length() - endSymbol.length() + 1; i++) {
			if (input.substring(i, i + startSymbol.length()).equals(startSymbol))
				parenthesis++;
			if (input.substring(i, i + endSymbol.length()).equals(endSymbol))
				parenthesis--;
			if (parenthesis == 0) {
				index = i;
				break;
			}
		}
		if (parenthesis != 0)
			throw new UnbalancedDescenderException(input, start);
		return index;
	}
}