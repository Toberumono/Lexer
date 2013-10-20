package lexer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.errors.EmptyInputException;
import lexer.errors.LexerException;
import lexer.errors.UnbalancedDescenderException;
import lexer.errors.UnrecognizedCharacterException;

public class Lexer {
	private final PairedList<String, Rule<?>> rules;
	private final PairedList<String, Descender> descenders;
	private final ArrayList<Type<Object>> types;
	private final ArrayList<Pattern> ignores;
	private final Stack<DescentSet> descentStack;
	private String input;
	private int head;
	private Token current, output;
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		rules = new PairedList<>();
		descenders = new PairedList<>();
		types = new ArrayList<Type<Object>>();
		ignores = new ArrayList<Pattern>();
		descentStack = new Stack<DescentSet>();
		input = "";
		head = 0;
		current = new Token();
		output = current;
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
		descentStack.push(new DescentSet(this.input, head, output));
		this.input = input;
		head = 0;
		try {
			while (head < input.length())
				current = current.append(getNextToken(true));
		}
		catch (LexerException e) {
			descentStack.clear();
			throw e;
		}
		this.input = descentStack.peek().getInput();
		head = descentStack.peek().getHead();
		output = descentStack.pop().getOutput();
		current = output.getLastToken();
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
		while (ignores.size() > 0) {
			Matcher m = null;
			for (int i = 0; i < ignores.size(); i++) {
				Matcher check = ignores.get(0).matcher(input);
				if (check.find() && check.start() == head && (m == null || check.end() > m.end()))
					m = check;
			}
			if (m == null)
				break;
			head = m.end();
		}
		if (head >= input.length())
			throw new EmptyInputException();
		Descender d = null;
		for (Descender descender : descenders.getValues())
			if (input.length() - head >= descender.open.length() && input.startsWith(descender.open, head) && (d == null || descender.open.length() > d.open.length()))
				d = descender;
		if (d != null) {
			int close = getEndIndex(input, head, d.open, d.close);
			Matcher m = Pattern.compile("\\Q" + input.substring(head + d.open.length(), close) + "\\E").matcher(input);
			m.find(head);
			Token result = d.apply(m, this);
			if (step) {
				this.head = close + d.close.length();
				while (head < input.length() && input.charAt(head) == ' ')
					head++;
			}
			return result;
		}
		if (rules.size() > 0) {
			Rule<?> hit = null;
			Matcher match = null;
			Matcher m;
			for (Rule<?> rule : rules.getValues()) {
				m = rule.pattern.matcher(input);
				if (!m.find(head) || m.start() != head || m.group().length() == 0)
					continue;
				if (match == null || match.group().length() < m.group().length()) {
					match = m;
					hit = rule;
				}
			}
			if (hit != null) {
				Token result = hit.apply(match, this);
				if (step) {
					head += match.group().length();
					while (head < input.length() && input.charAt(head) == ' ')
						head++;
				}
				return result;
			}
		}
		throw new UnrecognizedCharacterException(input, head);
	}
	
	/**
	 * @return the output this lexer is currently generating.
	 */
	public final Token getPreviousToken() {
		return current;
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
	 * Tells the lexer to skip over the given <tt>Pattern</tt>.
	 * 
	 * @param ignore
	 *            the <tt>Pattern</tt> to ignore
	 */
	public final void ignore(Pattern ignore) {
		ignores.add(ignore);
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
