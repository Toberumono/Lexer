package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;

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
	
	public String getInput() {
		return input;
	}
	
	public int getHead() {
		return head;
	}
	
	public D getDescender() {
		return descender;
	}
	
	public LexerState<To, Ty, R, D, L> advance(MatchResult result) {
		head = result.end();
		return this;
	}
	
	/**
	 * Increases the stored index by <tt>n</tt>
	 * 
	 * @param n
	 *            the amount by which to increase the stored index
	 */
	public void advance(int n) {
		head += n;
	}
	
	public int setHead(int index) {
		int oldIndex = this.head;
		this.head = index;
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
	
	public LexerState<To, Ty, R, D, L> descend(D descender) {
		return new LexerState<>(input, head, descender, lexer);
	}
	
	//TODO Test for ascent token
	/**
	 * This method returns true if there any untokenized input remains after skipping over tokens that are set to be ignored.
	 * 
	 * @return true if there is still untokenized input at the current descent level, otherwise false.
	 */
	public boolean hasNext() {
		return head + lexer.skipIgnores(this) < input.length();
	}
}
