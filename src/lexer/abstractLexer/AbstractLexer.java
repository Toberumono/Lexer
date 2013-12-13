package lexer.abstractLexer;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Type;
import lexer.errors.EmptyInputException;
import lexer.errors.LexerException;
import lexer.errors.UnbalancedDescenderException;
import lexer.errors.UnrecognizedCharacterException;
import lipstone.joshua.customStructures.lists.PairedList;

public abstract class AbstractLexer<T extends AbstractToken<? extends Type<?>, T>, U extends Type<?>, V extends AbstractRule<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, X>, ?, X>, W extends AbstractDescender<T, ? extends Type<T>, ? extends AbstractAction<T, ? extends Type<?>, ?, X>, X>, X extends AbstractLexer<T, ? extends Type<?>, V, W, X>> {
	protected final PairedList<String, V> rules;
	protected final PairedList<String, W> descenders;
	protected final ArrayList<U> types;
	protected final ArrayList<Pattern> ignores;
	protected final Stack<DescentSet<T>> descentStack;
	protected boolean ignoreSpace;
	protected String input;
	protected int head;
	protected T current, output;
	
	/**
	 * Basic constructor for a <tt>AbstractLexer</tt>
	 */
	public AbstractLexer() {
		rules = new PairedList<>();
		descenders = new PairedList<>();
		types = new ArrayList<>();
		ignores = new ArrayList<Pattern>();
		descentStack = new Stack<>();
		ignoreSpace = true;
		input = "";
		head = 0;
		current = makeNewToken();
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
	public final T lex(String input) throws LexerException {
		descentStack.push(new DescentSet<T>(this.input, head, output));
		this.input = input;
		current = makeNewToken();
		output = current;
		head = 0;
		try {
			while (head < input.length())
				if (hasNext())
					current = (T) current.append(getNextToken(true));
				else
					break;
		}
		catch (LexerException e) {
			descentStack.clear();
			throw e;
		}
		T result = output;
		this.input = descentStack.peek().getInput();
		head = descentStack.peek().getHead();
		output = descentStack.pop().getOutput();
		current = (T) output.getLastToken();
		return result;
	}
	
	/**
	 * Gets the next token in the input without stepping this <tt>AbstractLexer</tt> forward.
	 * 
	 * @return the next token in this <tt>AbstractLexer</tt>'s input
	 * @throws LexerException
	 *             if no token was found
	 */
	public final T getNextToken() throws LexerException {
		return getNextToken(false);
	}
	
	/**
	 * Finds the next token in this <tt>AbstractLexer</tt>
	 * 
	 * @param step
	 *            if this is true, it steps this <tt>AbstractLexer</tt>'s read-head forward
	 * @return the next token in this <tt>AbstractLexer</tt>'s input
	 * @throws LexerException
	 *             if no token was found
	 */
	public final T getNextToken(boolean step) throws LexerException {
		skipIgnores();
		if (head >= input.length())
			throw new EmptyInputException();
		W d = null;
		for (W descender : descenders.getValues())
			if (input.length() - head >= descender.open.length() && input.startsWith(descender.open, head) && (d == null || descender.open.length() > d.open.length()))
				d = descender;
		if (d != null) {
			int close = getEndIndex(input, head, d.open, d.close);
			Matcher m = Pattern.compile("\\Q" + input.substring(head + d.open.length(), close) + "\\E").matcher(input);
			m.find(head);
			int oldHead = head;
			head = close + d.close.length();
			T result = descend(d, m);
			if (!step)
				head = oldHead;
			return result;
		}
		if (rules.size() > 0) {
			V hit = null;
			Matcher match = null, m;
			for (V rule : rules.getValues()) {
				m = rule.getPattern().matcher(input);
				if (m.find(head) && m.group().length() != 0 && (match == null || match.group().length() < m.group().length())) {
					match = m;
					hit = rule;
				}
			}
			if (hit != null) {
				head += match.group().length();
				T result = hit(hit, match);
				if (!step)
					head -= match.group().length();
				return result;
			}
		}
		if (input.charAt(head) == ' ') {
			head++;
			return getNextToken(step);
		}
		throw new UnrecognizedCharacterException(input, head);
	}
	
	private final void skipIgnores() {
		while (true) {
			if (ignoreSpace) //This is true if none of patterns start with spaces.
				while (head < input.length() && input.charAt(head) == ' ')
					head++;
			Matcher m = null, check;
			//Get the longest match from the read-head in the ignore patterns
			for (Pattern p : ignores)
				if ((check = p.matcher(input)).find(head) && (m == null || check.end() > m.end()))
					m = check;
			//If nothing matched starting at the read-head, break
			if (m == null)
				break;
			//Otherwise, skip it
			head = m.end();
		}
	}
	
	private final boolean startsWithSpace(String regex) {
		if (regex.startsWith("\\G"))
			regex = regex.substring(2);
		if (regex.charAt(0) == ' ' || (regex.charAt(0) == '\\' && regex.length() > 1 && regex.charAt(1) == ' '))
			return true;
		if (regex.charAt(0) == '[') {
			try {
				return regex.substring(0, getEndIndex(regex, 0, "[", "]")).contains(" ");
			}
			catch (UnbalancedDescenderException e) {/*Cannot occur because the pattern is valid*/}
		}
		if (regex.charAt(0) == '(') {
			try {
				for (String option : regex.substring(0, getEndIndex(regex, 0, "(", ")")).split("(?<!\\\\)\\|"))
					if (startsWithSpace(option))
						return true;
			}
			catch (UnbalancedDescenderException e) {/*Cannot occur because the pattern is valid*/}
			return false;
		}
		return false;
	}
	
	/**
	 * @return the output this lexer is currently generating.
	 */
	public final T getPreviousToken() {
		return current;
	}
	
	/**
	 * @return true if there is still untokenized input, otherwise false
	 */
	public final boolean hasNext() {
		skipIgnores();
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
	public final void addRule(String name, V rule) {
		if (ignoreSpace)
			ignoreSpace = !startsWithSpace(rule.getPattern().pattern());
		rules.add(name, rule);
	}
	
	/**
	 * Adds a new descender
	 * 
	 * @param name
	 *            the name of the descender
	 * @param descender
	 *            the descender
	 */
	public final void addDescender(String name, W descender) {
		if (ignoreSpace)
			ignoreSpace = !(descender.open.charAt(0) == ' ' || descender.close.charAt(0) == ' ');
		descenders.add(name, descender);
	}
	
	/**
	 * Tells the lexer to skip over the <tt>Pattern</tt> in the given regex <tt>String</tt>.
	 * 
	 * @param ignore
	 *            the <tt>Pattern</tt> to ignore as a regex <tt>String</tt>
	 */
	public final void ignore(String ignore) {
		ignores.add(Pattern.compile(ignore.startsWith("\\G") ? ignore : "\\G" + ignore));
	}
	
	/**
	 * Tells the lexer to skip over the <tt>Pattern</tt> in the given regex <tt>String</tt>.
	 * 
	 * @param ignore
	 *            the <tt>Pattern</tt> to ignore as a regex <tt>String</tt>
	 * @param flags
	 *            the regex flags defined in {@link java.util.regex.Pattern Pattern}
	 */
	public final void ignore(String ignore, int flags) {
		ignores.add(Pattern.compile((ignore.startsWith("\\G") ? ignore : "\\G" + ignore), flags));
	}
	
	/**
	 * @return the types that this lexer can find.
	 */
	public ArrayList<U> getTypes() {
		return types;
	}
	
	protected int getEndIndex(String input, int start, String startSymbol, String endSymbol) throws UnbalancedDescenderException {
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
			if (input.charAt(i) == '\\') {
				i++;
				continue;
			}
		}
		if (parenthesis != 0)
			throw new UnbalancedDescenderException(input, start);
		return index;
	}
	
	/**
	 * Due to how type-erasure works, this method must be initialized in subclasses with the following code:</br>
	 * <code>return new {@literal <}class extending <tt>Token</tt>{@literal >}();</code>
	 */
	public abstract T makeNewToken();
	
	/**
	 * Due to how type-erasure works, this method must be initialized in subclasses with the following code:</br>
	 * <code>return d.apply(m, this);</code>
	 */
	protected abstract T descend(W d, Matcher m) throws LexerException;
	
	/**
	 * Due to how type-erasure works, this method must be initialized in subclasses with the following code:</br>
	 * <code>return r.apply(m, this);</code>
	 */
	protected abstract T hit(V r, Matcher m) throws LexerException;
}
