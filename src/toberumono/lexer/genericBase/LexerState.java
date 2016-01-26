package toberumono.lexer.genericBase;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * A container that stores the state information of a lexing operation.<br>
 * These are usually created during a call to {@link GenericLexer#lex(String)}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link GenericRule} to be used
 * @param <D>
 *            the implementation of {@link GenericDescender} to be used
 * @param <L>
 *            the implementation of {@link GenericLexer} to be used
 */
public class LexerState<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	private final String input;
	private final D descender;
	private final L lexer;
	private final GenericLanguage<C, T, R, D, L> language;
	private int head;
	private C root, last;
	
	/**
	 * Constructs a new {@link LexerState} with the given state information. This should generally only be called from
	 * {@link GenericLexer#lex(String)}.
	 * 
	 * @param input
	 *            the input to be lexed
	 * @param head
	 *            the position from which the next match must start
	 * @param descender
	 *            the most recent descender
	 * @param lexer
	 *            the {@link GenericLexer Lexer} for which the {@link LexerState} was created
	 */
	public LexerState(String input, int head, D descender, GenericLexer<C, T, R, D, L> lexer) {
		this(input, head, descender, lexer, lexer.getLanguage());
	}
	
	/**
	 * Constructs a new {@link LexerState} with the given state information. This should generally only be called from
	 * {@link GenericLexer#lex(String)}.
	 * 
	 * @param input
	 *            the input to be lexed
	 * @param head
	 *            the position from which the next match must start
	 * @param descender
	 *            the most recent descender
	 * @param lexer
	 *            the {@link GenericLexer Lexer} for which the {@link LexerState} was created
	 * @param language
	 *            the {@link GenericLanguage Language} that the {@link LexerState} is to use
	 */
	@SuppressWarnings("unchecked")
	public LexerState(String input, int head, D descender, GenericLexer<C, T, R, D, L> lexer, GenericLanguage<C, T, R, D, L> language) {
		this.input = input;
		this.head = head;
		this.descender = descender;
		this.lexer = (L) lexer;
		this.language = language;
		last = root = null;
	}
	
	/**
	 * @return the input {@link String} being lexed
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * @return the index from which the next cell will be matched
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
	 * @return the root cell of the resulting cell tree (the left-most cell)
	 */
	public C getRoot() {
		return root;
	}
	
	/**
	 * Appends the given cell to the cell tree.
	 * 
	 * @param cell
	 *            the cell to append
	 * @return {@code this} for easy chaining
	 */
	public LexerState<C, T, R, D, L> appendMatch(C cell) {
		if (root == null)
			root = last = cell;
		else
			last = last.append(cell);
		return this;
	}
	
	/**
	 * Gets the most recently appended <tt>ConsCell</tt> from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> <tt>ConsCell</tt>, just the last <tt>ConsCell</tt> that
	 * was appended to the output.<br>
	 * In order to remove the <tt>ConsCell</tt> from the output, use {@link #popPreviousConsCell()}
	 * 
	 * @return the most recently appended <tt>ConsCell</tt> or {@code null} if no such <tt>ConsCell</tt> exists (this occurs
	 *         if there has yet to be a match or all of the matched <tt>ConsCells</tt> were popped via
	 *         {@link #popPreviousConsCell()})
	 * @see #popPreviousConsCell()
	 */
	public C getPreviousConsCell() {
		return last;
	}
	
	/**
	 * Removes the most recently appended <tt>ConsCell</tt> from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> <tt>ConsCell</tt>, just the last <tt>ConsCell</tt> that
	 * was appended to the output.<br>
	 * Use {@link #getPreviousConsCell()} to get the <tt>ConsCell</tt> without removing it.
	 * 
	 * @return the most recently appended <tt>ConsCell</tt> or {@code null} if no such <tt>ConsCell</tt> exists (this occurs
	 *         if there has yet to be a match or all of the matched <tt>ConsCells</tt> were popped via
	 *         {@link #popPreviousConsCell()})
	 * @see #getPreviousConsCell()
	 */
	public C popPreviousConsCell() {
		if (last == null)
			return last;
		C ret = last;
		if (last == root)
			last = root = null;
		else {
			last = last.getPreviousConsCell();
			ret.remove();
		}
		return ret;
	}
	
	/**
	 * Constructs a new {@link LexerState} with the same fields but null cells and the descender set to <tt>descender</tt>.
	 * 
	 * @param descender
	 *            the {@link GenericDescender Descender} that was encountered
	 * @return a separate {@link LexerState} that is used to watch for the correct close cell
	 */
	public LexerState<C, T, R, D, L> descend(D descender) {
		return new LexerState<>(input, head, descender, lexer);
	}
	
	/**
	 * This method returns true if there any uncellized input remains after skipping over cells that are set to be ignored
	 * and the next matched cell would not be an ascent cell.<br>
	 * <b><i>Note</i></b>: This is <i>slow</i> - the {@link GenericLexer Lexer} already performs these checks before getting
	 * the next cell, so if you are calling this regularly, consider re-working the logic behind your rules.
	 * 
	 * @return true if there is still uncellized input at the current descent level, otherwise false.
	 */
	public boolean hasNext() {
		if (head + lexer.skipIgnores(this) < input.length()) {
			if (descender != null) {
				Matcher longest = null;
				for (Pattern p : lexer.getPatterns().keySet()) {
					Matcher m = p.matcher(input);
					if (m.find(head) && m.start() == head && (longest == null || m.end() > longest.end() || (descender != null && m.end() == longest.end() && p == descender.close)))
						longest = m;
				}
				return longest.pattern() != descender.close;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @return the {@link GenericLanguage Language} in use
	 */
	public GenericLanguage<C, T, R, D, L> getLanguage() {
		return language;
	}
	
	/**
	 * Produces a shallow copy of this {@link LexerState} with the given {@link GenericLanguage Language}. This method can be
	 * called from any {@link GenericAction}.
	 * 
	 * @param language
	 *            the new {@link GenericLanguage Language} to use
	 * @return a shallow copy of this {@link LexerState} with the given {@link GenericLanguage Language}
	 */
	public LexerState<C, T, R, D, L> setLanguage(GenericLanguage<C, T, R, D, L> language) {
		return new LexerState<>(input, head, descender, lexer, language);
	}
}
