package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A container that stores the state information of a lexing operation.<br>
 * These are usually created during a call to {@link GenericLexer#lex(String)}.
 * 
 * @author Toberumono
 * @param <To>
 *            the implementation of {@link GenericToken} to be used
 * @param <Ty>
 *            the implementation of {@link GenericType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class LexerState<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>> {
	private final String input;
	private final D descender;
	private final L lexer;
	private int head;
	private To root, last;
	
	@SuppressWarnings("unchecked")
	LexerState(String input, int head, D descender, GenericLexer<To, Ty, R, D, L> lexer) {
		this.input = input;
		this.head = head;
		this.descender = descender;
		this.lexer = (L) lexer;
		last = root = null;
	}
	
	/**
	 * @return the input {@link String} being lexed
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * @return the index from which the next token will be matched
	 */
	public int getHead() {
		return head;
	}
	
	/**
	 * @return the {@link GenericDescender Descender} that created this {@link LexerState}. This is <i>often</i> {@code null}
	 */
	public D getDescender() {
		return descender;
	}
	
	/**
	 * Sets the head position to the value of {@link MatchResult#end()} for the given <tt>match</tt>
	 * 
	 * @param match
	 *            the match with which to update the head position
	 */
	public void advance(MatchResult match) {
		head = match.end();
	}
	
	/**
	 * Increases the stored head position by <tt>n</tt>
	 * 
	 * @param n
	 *            the amount by which to increase the stored index
	 */
	public void advance(int n) {
		head += n;
	}
	
	/**
	 * Sets the position of the head.
	 * 
	 * @param pos
	 *            the new position of the head
	 * @return the previous position of the head
	 */
	public int setHead(int pos) {
		int oldIndex = this.head;
		this.head = pos;
		return oldIndex;
	}
	
	/**
	 * @return the root token of the resulting token tree (the left-most token)
	 */
	public To getRoot() {
		return root;
	}
	
	/**
	 * Appends the given token to the token tree.
	 * 
	 * @param token
	 *            the token to append
	 * @return {@code this} for easy chaining
	 */
	public LexerState<To, Ty, R, D, L> appendMatch(To token) {
		if (root == null)
			root = last = token;
		else
			last = last.append(token);
		return this;
	}
	
	/**
	 * Gets the most recently appended <tt>Token</tt> from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> <tt>Token</tt>, just the last <tt>Token</tt> that was
	 * appended to the output.<br>
	 * In order to remove the <tt>Token</tt> from the output, use {@link #popPreviousToken()}
	 * 
	 * @return the most recently appended <tt>Token</tt>
	 * @see #popPreviousToken()
	 */
	public To getPreviousToken() {
		return last;
	}
	
	/**
	 * Removes the most recently appended <tt>Token</tt> from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> <tt>Token</tt>, just the last <tt>Token</tt> that was
	 * appended to the output.<br>
	 * Use {@link #getPreviousToken()} to get the <tt>Token</tt> without removing it.
	 * 
	 * @return the most recently appended <tt>Token</tt>
	 * @see #getPreviousToken()
	 */
	public To popPreviousToken() {
		To ret = last;
		if (last == root)
			last = root = null;
		else {
			last = last.getPreviousToken();
			ret.remove();
		}
		return ret;
	}
	
	/**
	 * Constructs a new {@link LexerState} with the same fields but null tokens and the descender set to <tt>descender</tt>.
	 * 
	 * @param descender
	 *            the {@link GenericDescender Descender} that was encountered
	 * @return a separate {@link LexerState} that is used to watch for the correct close token
	 */
	public LexerState<To, Ty, R, D, L> descend(D descender) {
		return new LexerState<>(input, head, descender, lexer);
	}
	
	/**
	 * This method returns true if there any untokenized input remains after skipping over tokens that are set to be ignored
	 * and the next matched token would not be an ascent token.<br>
	 * <b><i>Note</i></b>: This is <i>slow</i> - the {@link GenericLexer Lexer} already performs these checks before getting
	 * the next token, so if you are calling this regularly, consider re-working the logic behind your rules.
	 * 
	 * @return true if there is still untokenized input at the current descent level, otherwise false.
	 */
	public boolean hasNext() {
		if (head + lexer.skipIgnores(this) < input.length()) {
			if (descender != null) {
				Matcher longest = null;
				for (Pattern p : lexer.patterns.keySet()) {
					Matcher m = p.matcher(input);
					if (m.find(head) && m.start() == head && (longest == null || m.end() > longest.end()))
						longest = m;
				}
				return longest.pattern() != descender.close;
			}
			return true;
		}
		return false;
	}
}
