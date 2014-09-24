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

public abstract class AbstractLexer<T extends AbstractToken<? extends Type<?>, T>, U extends Type<?>, V extends AbstractRule<T, ? extends Type<?>, ?, L>, W extends AbstractDescender<T, ? extends Type<T>, L>, L extends AbstractLexer<T, ? extends Type<?>, V, W, L>> {
	protected final PairedList<String, V> rules = new PairedList<>();
	protected final PairedList<String, W> descenders = new PairedList<>();
	protected final ArrayList<U> types = new ArrayList<>();
	protected final ArrayList<Pattern> ignores = new ArrayList<>();
	protected final Stack<DescentSet<T>> descentStack = new Stack<>();
	protected boolean ignoreSpace = true;
	protected String input = "";
	protected int head = 0;
	protected T current, output, previous;
	private final TokenConstructor<U, T> tokenConstructor;
	
	/**
	 * Constructs a <tt>GenericLexer</tt> with the provided token constructor
	 * 
	 * @param tokenConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending {@link AbstractToken}.
	 */
	public AbstractLexer(TokenConstructor<U, T> tokenConstructor) {
		this.tokenConstructor = tokenConstructor;
		previous = output = current = tokenConstructor.makeNewToken();
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 */
	public T lex(String input) throws LexerException {
		return lex(input, 0);
	}
	
	/**
	 * Tokenizes a <tt>String</tt> that is the modified version of a previously tokenized <tt>String</tt> from a given
	 * starting point.
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @param head
	 *            the location at which to start lexing the input
	 * @param output
	 *            the output from the previous tokenization
	 * @param previous
	 *            the last token in the previous tokenization
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 */
	public T lex(String input, int head, T output, T previous) throws LexerException {
		descentStack.push(new DescentSet<T>(this.input, this.head, this.output, this.previous, current));
		this.previous = previous;
		current = previous;
		this.head = head;
		this.output = output;
		try {
			while (this.head < input.length())
				if (hasNext())
					current = previous = (T) current.append(getNextToken(true));
				else
					break;
		}
		catch (LexerException e) {
			descentStack.clear();
			throw e;
		}
		T result = output;
		DescentSet<T> popped = descentStack.pop();
		this.input = popped.getInput();
		this.head = popped.getHead();
		previous = popped.getPrevious();
		output = popped.getOutput();
		current = popped.getCurrent();
		return result;
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @param head
	 *            the location at which to start lexing the input
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws LexerException
	 */
	public T lex(String input, int head) throws LexerException {
		descentStack.push(new DescentSet<T>(this.input, this.head, output, previous, current));
		this.input = input;
		current = tokenConstructor.makeNewToken();
		output = previous = current;
		this.head = head;
		try {
			while (this.head < input.length())
				if (hasNext())
					current = previous = (T) current.append(getNextToken(true));
				else
					break;
		}
		catch (LexerException e) {
			descentStack.clear();
			throw e;
		}
		T result = output;
		DescentSet<T> popped = descentStack.pop();
		this.input = popped.getInput();
		this.head = popped.getHead();
		previous = popped.getPrevious();
		output = popped.getOutput();
		current = popped.getCurrent();
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
	public T getNextToken(boolean step) throws LexerException {
		skipIgnores();
		if (head >= input.length())
			throw new EmptyInputException();
		W d = null;
		for (W descender : descenders.getValues())
			if (input.length() - head >= descender.open.length() && input.startsWith(descender.open, head) && (d == null || descender.open.length() > d.open.length()))
				d = descender;
		if (d != null) {
			int close = getEndIndex(input, head, d.open, d.close);
			int oldHead = head;
			head = close + d.close.length();
			T result = d.apply(input.substring(oldHead + d.open.length(), close), (L) this);
			if (!step)
				head = oldHead;
			else
				previous = result;
			return result;
		}
		if (rules.size() > 0) {
			V hit = null;
			Matcher match = null, m;
			for (V rule : rules.getValues()) {
				m = rule.pattern.matcher(input);
				if (m.find(head) && m.group().length() != 0 && (match == null || match.group().length() < m.group().length())) {
					match = m;
					hit = rule;
				}
			}
			if (hit != null) {
				head += match.group().length();
				T result = hit.apply(match, (L) this);
				if (!step)
					head -= match.group().length();
				else
					previous = result;
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
		return previous;
	}
	
	/**
	 * Removes the last generated token and returns it.
	 * 
	 * @return the output this lexer is currently generating.
	 */
	public final T popPreviousToken() {
		T temp = previous;
		previous = current == previous ? (current = previous.getPreviousToken()) : previous.getPreviousToken();
		temp.remove();
		return temp;
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
			ignoreSpace = !startsWithSpace(rule.pattern.pattern());
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
	
	public final TokenConstructor<U, T> getTokenConstructor() {
		return tokenConstructor;
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
}

class DescentSet<T extends AbstractToken<?, T>> {
	final String input;
	final int head;
	final T output, previous, current;
	
	public DescentSet(String input, int head, T output, T previous, T current) {
		this.input = input;
		this.head = head;
		this.output = output;
		this.previous = previous;
		this.current = current;
	}
	
	public String getInput() {
		return input;
	}
	
	public int getHead() {
		return head;
	}
	
	public T getOutput() {
		return output;
	}
	
	public T getPrevious() {
		return previous;
	}
	
	public T getCurrent() {
		return current;
	}
}
