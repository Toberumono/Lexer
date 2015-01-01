package lipstone.joshua.lexer.abstractLexer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lipstone.joshua.lexer.errors.EmptyInputException;
import lipstone.joshua.lexer.errors.UnbalancedDescenderException;
import lipstone.joshua.lexer.errors.UnrecognizedCharacterException;

public abstract class AbstractLexer<To extends AbstractToken<Ty, To>, Ty extends AbstractType, R extends AbstractRule<To, Ty, L>, D extends AbstractDescender<To, Ty, L>, L extends AbstractLexer<To, Ty, R, D, L>> {
	protected final LinkedHashMap<String, R> rules = new LinkedHashMap<>();
	protected final LinkedHashMap<String, D> descenders = new LinkedHashMap<>();
	protected final LinkedHashMap<String, Pattern> ignores = new LinkedHashMap<>();
	protected final ArrayList<Ty> types = new ArrayList<>();
	protected final Stack<DescentSet<To>> descentStack = new Stack<>();
	protected final Stack<Integer> headStack = new Stack<>();
	protected final Stack<String> closeTokenStack = new Stack<>();
	protected boolean ignoreSpace = true;
	protected String input = "";
	protected int head = 0;
	protected To current, output, previous;
	private final TokenConstructor<Ty, To> tokenConstructor;
	protected final Ty emptyType;
	
	/**
	 * Constructs an <tt>AbstractLexer</tt> with the provided token constructor that will skip over spaces in the input.
	 * 
	 * @param tokenConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending {@link AbstractToken}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 */
	public AbstractLexer(TokenConstructor<Ty, To> tokenConstructor, Ty emptyType) {
		this(tokenConstructor, emptyType, true);
	}
	
	/**
	 * Constructs an <tt>AbstractLexer</tt> with the provided token constructor
	 * 
	 * @param tokenConstructor
	 *            a function that takes no arguments and returns a new instance of the class extending {@link AbstractToken}.
	 * @param emptyType
	 *            the <tt>Type</tt> that represents an empty (or null) value in the <tt>Token</tt> type that this
	 *            <tt>Lexer</tt> uses.
	 * @param ignoreSpace
	 *            whether to ignore spaces in an input by default
	 */
	public AbstractLexer(TokenConstructor<Ty, To> tokenConstructor, Ty emptyType, boolean ignoreSpace) {
		this.tokenConstructor = tokenConstructor;
		previous = output = current = tokenConstructor.makeNewToken(null, emptyType, null, emptyType);
		this.ignoreSpace = ignoreSpace;
		this.emptyType = emptyType;
		if (ignoreSpace)
			ignore("Space", Pattern.compile(" +"));
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws Exception
	 *             so that lexer exceptions can be propogated back to the original caller
	 */
	public To lex(String input) throws Exception {
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
	 * @throws Exception
	 *             so that lexer exceptions can be propogated back to the original caller
	 */
	public To lex(String input, int head, To output, To previous) throws Exception {
		descentStack.push(new DescentSet<To>(this.input, this.head, this.output, this.previous, current));
		this.previous = previous;
		current = previous;
		this.head = head;
		this.output = output;
		return lexLoop();
	}
	
	/**
	 * Tokenizes a <tt>String</tt>
	 * 
	 * @param input
	 *            the <tt>String</tt> to tokenize
	 * @param head
	 *            the location at which to start lexing the input
	 * @return the <tt>Token</tt>s in the <tt>String</tt>
	 * @throws Exception
	 *             so that lexer exceptions can be propogated back to the original caller
	 */
	public To lex(String input, int head) throws Exception {
		descentStack.push(new DescentSet<To>(this.input, this.head, output, previous, current));
		this.input = input;
		current = tokenConstructor.makeNewToken(null, emptyType, null, emptyType);
		output = previous = current;
		this.head = head;
		return lexLoop();
	}
	
	private To lexLoop() throws Exception {
		try {
			while (head < input.length()) {
				skipIgnores();
				if (closeTokenStack.size() > 0 && input.startsWith(closeTokenStack.peek(), head)) {
					head += closeTokenStack.pop().length();
					return ascend();
				}
				else if (hasNext()) {
					To t = getNextToken(true);
					current = previous = (To) current.append(t);
				}
				else
					break;
			}
		}
		catch (Exception e) {
			descentStack.clear();
			throw e;
		}
		To result = output;
		ascend();
		return result;
	}
	
	/**
	 * Moves the append head up one level in the tree and returns the result of parsing the level that was just left.
	 * 
	 * @return the result of parsing the level that was just left.
	 */
	private final To ascend() {
		To result = output;
		DescentSet<To> popped = descentStack.pop();
		input = popped.getInput();
		previous = popped.getPrevious();
		output = popped.getOutput();
		current = popped.getCurrent();
		return result;
	}
	
	/**
	 * @return the index that this <tt>AbstractLexer</tt> has reached in the input.
	 */
	public final int getHeadIndex() {
		return head;
	}
	
	/**
	 * Gets the next token in the input without stepping this <tt>AbstractLexer</tt> forward.
	 * 
	 * @return the next token in this <tt>AbstractLexer</tt>'s input
	 * @throws Exception
	 *             so that exception handling can take place in the calling function
	 */
	public final To getNextToken() throws Exception {
		return getNextToken(false);
	}
	
	/**
	 * Finds the next token in this <tt>AbstractLexer</tt>
	 * 
	 * @param step
	 *            if this is true, it steps this <tt>AbstractLexer</tt>'s read-head forward
	 * @return the next token in this <tt>AbstractLexer</tt>'s input
	 * @throws Exception
	 *             so that exception handling can take place in the calling function
	 */
	@SuppressWarnings("unchecked")
	public To getNextToken(boolean step) throws Exception {
		do {
			if (head >= input.length())
				throw new EmptyInputException();
			int oldHead = head;
			To result = null;
			D d = null;
			R hit = null;
			Matcher match = null, m;
			//Descenders
			for (D descender : descenders.values())
				if (input.length() - head >= descender.open.length() && input.startsWith(descender.open, head) && (d == null || descender.open.length() > d.open.length()))
					d = descender;
			if (d != null) {
				try {
					closeTokenStack.push(d.close);
					d.openAction.perform((L) this);
					result = d.closeAction.perform(lex(input, head + d.open.length()), (L) this);
				}
				catch (UnbalancedDescenderException e) {
					throw new UnbalancedDescenderException(input, head); //Corrects the exception so that it outputs the correct descender.
				}
				if (!step)
					head = oldHead;
				else
					previous = result;
				return result;
			}
			
			//Rules
			for (R rule : rules.values())
				if ((m = rule.pattern.matcher(input)).find(head) && m.start() == head && m.group().length() != 0 && (match == null || match.end() < m.end())) {
					match = m;
					hit = rule;
				}
			if (hit != null) {
				head = match.end();
				result = hit.action.perform(match, (L) this);
				if (!step)
					head = oldHead;
				else
					previous = result;
				return result;
			}
		} while (skipIgnores() > 0);
		throw new UnrecognizedCharacterException(input, head);
	}
	
	/**
	 * Skips over tokens that are set to be ignored.
	 * 
	 * @return the number of characters that was skipped
	 */
	private final int skipIgnores() {
		int oldHead = head;
		while (true) {
			Matcher match = null, m;
			for (Pattern ignore : ignores.values())
				if ((m = ignore.matcher(input)).find(head) && m.start() == head && (match == null || match.end() < m.end()))
					match = m;
			if (match != null)
				head = match.end();
			else
				break;
		}
		return head - oldHead;
	}
	
	/**
	 * In order to remove any <tt>Token</tt> from the output, use {@link #popPreviousToken()}
	 * 
	 * @return the last <tt>Token</tt> in the output this <tt>Lexer</tt> is currently generating.
	 * @see #popPreviousToken()
	 */
	public final To getPreviousToken() {
		return previous;
	}
	
	/**
	 * Removes the most recently appended <tt>Token</tt> from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> <tt>Token</tt>, just the last <tt>Token</tt> that was
	 * appended to the output.<br>
	 * Use {@link #getPreviousToken()} to get this <tt>Token</tt> without removing it.
	 * 
	 * @return the most recently appended <tt>Token</tt>.
	 * @see #getPreviousToken()
	 */
	public final To popPreviousToken() {
		To temp = previous;
		if (current == output)
			output = previous = (current == previous ? (current = previous.getPreviousToken()) : (current = previous).getPreviousToken());
		else
			previous = (current == previous ? (current = previous.getPreviousToken()) : (current = previous).getPreviousToken());
		temp.remove();
		return temp;
	}
	
	/**
	 * This method returns true if there any untokenized input remains in the lexer after skipping over tokens that are set
	 * to be ignored. This method will return <tt>false</tt> if the input starting at the head index is an appropriate ascent
	 * token for the current tree level.
	 * 
	 * @return true if there is still untokenized input at the current descent level, otherwise false.
	 */
	public final boolean hasNext() {
		skipIgnores();
		return head < input.length() && (closeTokenStack.size() == 0 || !input.startsWith(closeTokenStack.peek(), head));
	}
	
	/**
	 * Adds a new rule
	 * 
	 * @param name
	 *            the name of the rule
	 * @param rule
	 *            the rule
	 */
	public void addRule(String name, R rule) {
		rules.put(name, rule);
	}
	
	/**
	 * Removes a rule
	 * 
	 * @param name
	 *            the name of the rule to remove
	 * @return the removed rule if a rule of that name existed, otherwise null
	 */
	public R removeRule(String name) {
		return rules.remove(name);
	}
	
	/**
	 * Gets a rule by name
	 * 
	 * @param name
	 *            the name of the rule to get
	 * @return the rule if a rule corresponding to that name is loaded, otherwise null
	 */
	public R getRule(String name) {
		return rules.get(name);
	}
	
	/**
	 * Adds a new descender
	 * 
	 * @param name
	 *            the name of the descender
	 * @param descender
	 *            the descender
	 */
	public void addDescender(String name, D descender) {
		descenders.put(name, descender);
	}
	
	/**
	 * Removes a descender
	 * 
	 * @param name
	 *            the name of the descender to remove
	 * @return the removed descender if a descender of that name existed, otherwise null
	 */
	public D removeDescender(String name) {
		return descenders.remove(name);
	}
	
	/**
	 * Gets a descender by name
	 * 
	 * @param name
	 *            the name of the descender to get
	 * @return the descender if a descender corresponding to that name is loaded, otherwise null
	 */
	public D getDescender(String name) {
		return descenders.get(name);
	}
	
	/**
	 * Tells the lexer to skip over the <tt>Pattern</tt> in the given regex <tt>String</tt>.
	 * 
	 * @param name
	 *            the name with which to reference this ignore pattern
	 * @param ignore
	 *            the <tt>Pattern</tt> to ignore
	 */
	public final void ignore(String name, Pattern ignore) {
		if (ignores.containsKey(name))
			ignores.remove(name);
		ignores.put(name, ignore);
	}
	
	/**
	 * @return the types that this lexer can find.
	 */
	public ArrayList<Ty> getTypes() {
		return types;
	}
	
	public final TokenConstructor<Ty, To> getTokenConstructor() {
		return tokenConstructor;
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
