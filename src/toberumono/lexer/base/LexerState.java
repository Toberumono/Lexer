package toberumono.lexer.base;

import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;
import toberumono.structures.sexpressions.GenericConsCell;

/**
 * A container that stores the state information of a lexing operation.<br>
 * These are usually created during a call to {@link Lexer#lex(String)}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link ConsCell} to be used
 * @param <T>
 *            the implementation of {@link ConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 */
public class LexerState<C extends GenericConsCell<C, T>, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	private final String input;
	private final D descender;
	private L lexer;
	private Stack<Language<C, T, R, D, L>> language;
	private int head;
	private C root, last;
	
	/**
	 * Constructs a new {@link LexerState} with the given state information. This should generally only be called from {@link Lexer#lex(String)}.
	 * 
	 * @param input
	 *            the input to be tokenized
	 * @param head
	 *            the position from which the next match must start
	 * @param descender
	 *            the most recent descender
	 * @param lexer
	 *            the {@link Lexer} for which the {@link LexerState} was created
	 */
	public LexerState(String input, int head, D descender, L lexer) {
		this(input, head, descender, lexer, lexer.getLanguage());
	}
	
	/**
	 * Constructs a new {@link LexerState} with the given state information. This should generally only be called from {@link Lexer#lex(String)}.
	 * 
	 * @param input
	 *            the input to be tokenized
	 * @param head
	 *            the position from which the next match must start
	 * @param descender
	 *            the most recent descender
	 * @param lexer
	 *            the {@link Lexer} for which the {@link LexerState} was created
	 * @param language
	 *            the {@link Language} that the {@link LexerState} is to use
	 */
	public LexerState(String input, int head, D descender, L lexer, Language<C, T, R, D, L> language) {
		this(input, head, descender, lexer, new Stack<>());
		this.language.push(language);
	}
	
	/**
	 * Constructs a new {@link LexerState} with the given state information. This should generally only be called from {@link #descend(Descender)}.
	 * 
	 * @param input
	 *            the input to be tokenized
	 * @param head
	 *            the position from which the next match must start
	 * @param descender
	 *            the most recent descender
	 * @param lexer
	 *            the {@link Lexer} for which the {@link LexerState} was created
	 * @param language
	 *            the {@link Language} that the {@link LexerState} is to use
	 */
	public LexerState(String input, int head, D descender, L lexer, Stack<Language<C, T, R, D, L>> language) {
		this.input = input;
		this.head = head;
		this.descender = descender;
		this.lexer = lexer;
		this.language = language;
		last = root = null;
	}
	
	private LexerState(LexerState<C, T, R, D, L> base, C root, C last) {
		this(base.getInput(), base.getHead(), base.getDescender(), base.lexer, base.getLanguage());
		this.root = root;
		this.last = last;
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
	 * @return the {@link Descender} that created this {@link LexerState}. This is <i>often</i> {@code null}
	 */
	public D getDescender() {
		return descender;
	}
	
	/**
	 * Sets the head position to the value of {@link MatchResult#end()} for the given {@code match}
	 * 
	 * @param match
	 *            the match with which to update the head position
	 */
	public void advance(MatchResult match) {
		setHead(match.end());
	}
	
	/**
	 * Increases the stored head position by {@code n}
	 * 
	 * @param n
	 *            the amount by which to increase the stored index
	 */
	public void advance(int n) {
		setHead(getHead() + n);
	}
	
	/**
	 * Sets the position of the head.
	 * 
	 * @param pos
	 *            the new position of the head
	 * @return the previous position of the head
	 */
	public int setHead(int pos) {
		int oldHead = getHead();
		head = pos;
		return oldHead;
	}
	
	/**
	 * @return the root cell of the resulting cell tree (the left-most cell)
	 */
	public C getRoot() {
		return root;
	}
	
	/**
	 * Sets the pointer to the most root {@code ConsCell}.
	 * 
	 * @param root
	 *            the root {@code ConsCell}
	 */
	public void setRoot(C root) {
		this.root = root;
	}
	
	/**
	 * Appends the given cell to the cell tree.
	 * 
	 * @param cell
	 *            the cell to append
	 * @return {@code this} for easy chaining
	 */
	public LexerState<C, T, R, D, L> appendMatch(C cell) {
		if (root == null) {
			setRoot(cell);
			setLast(cell.getLast());
		}
		else
			setLast(getLast().append(cell));
		return this;
	}
	
	/**
	 * Gets the most recently appended {@code ConsCell} from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> {@code ConsCell}, just the last {@code ConsCell} that was appended to the
	 * output.<br>
	 * In order to remove the {@code ConsCell} from the output, use {@link #popLast()}
	 * 
	 * @return the most recently appended {@code ConsCell} or {@code null} if no such {@code ConsCell} exists (this occurs if there has yet to be a
	 *         match or all of the matched {@code ConsCells} were popped via {@link #popLast()})
	 * @see #popLast()
	 */
	public C getLast() {
		return last;
	}
	
	/**
	 * Sets the pointer to the most recently appended {@code ConsCell}.
	 * 
	 * @param last
	 *            the most recently appended {@code ConsCell}
	 * @return {@code last} for chaining purposes
	 */
	public C setLast(C last) {
		return this.last = last;
	}
	
	/**
	 * Removes the most recently appended {@code ConsCell} from the output and returns it.<br>
	 * <b>NOTE</b>: This is not necessarily the last <i>matched</i> {@code ConsCell}, just the last {@code ConsCell} that was appended to the
	 * output.<br>
	 * Use {@link #getLast()} to get the {@code ConsCell} without removing it.
	 * 
	 * @return the most recently appended {@code ConsCell} or {@code null} if no such {@code ConsCell} exists (this occurs if there has yet to be a
	 *         match or all of the matched {@code ConsCells} were popped via {@link #popLast()})
	 * @see #getLast()
	 */
	public C popLast() {
		if (getLast() == null)
			return getLast();
		C ret = getLast();
		if (getLast() == getRoot())
			setRoot(setLast(null));
		else {
			setLast(getLast().getPrevious());
			ret.remove();
		}
		return ret;
	}
	
	/**
	 * Constructs a new {@link LexerState} with the same fields but {@code null} cells and the descender set to {@code descender}.
	 * 
	 * @param descender
	 *            the {@link Descender} that was encountered
	 * @return a separate {@link LexerState} that is used to watch for the correct close cell
	 */
	public LexerState<C, T, R, D, L> descend(D descender) {
		return new LexerState<>(getInput(), getHead(), descender, getLexer(), language);
	}
	
	/**
	 * This method returns true if any untokenized input remains after skipping over cells that are set to be ignored and the next matched cell would
	 * not be an ascent cell.<br>
	 * <b>Note</b>: This is <i>slow</i> - the {@link Lexer} already implicitly performs these checks before getting the next cell, so if you are
	 * calling this regularly, consider re-working the logic behind your rules.
	 * 
	 * @return {@code true} if there is still untokenized input at the current descent level, otherwise {@code false}.
	 */
	public boolean hasNext() {
		if (getHead() + getLexer().skipIgnores(this) < getInput().length()) {
			if (getDescender() != null) {
				Matcher longest = null;
				for (Pattern p : getLexer().getPatterns().keySet()) {
					Matcher m = p.matcher(getInput());
					if (m.find(getHead()) && m.start() == getHead() &&
							(longest == null || m.end() > longest.end() || (getDescender() != null && m.end() == longest.end() && p == getDescender().getClosePattern())))
						longest = m;
				}
				return longest.pattern() != getDescender().getClosePattern();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @return the {@link Lexer} in use
	 */
	public L getLexer() {
		return lexer;
	}
	
	/**
	 * @return the {@link Language} in use
	 */
	public Language<C, T, R, D, L> getLanguage() {
		return language.peek();
	}
	
	/**
	 * Pops the active {@link Language} from the {@link LexerState LexerState's} {@link Language} stack. This effectively reverts to the last-active
	 * {@link Language}.
	 * 
	 * @return the popped {@link Language}
	 */
	public Language<C, T, R, D, L> popLanguage() {
		return language.pop();
	}
	
	/**
	 * Pushes the {@link Language} onto the {@link LexerState LexerState's} {@link Language} stack. This changes the {@link LexerState LexerState's}
	 * active {@link Language} while also providing a change history.
	 * 
	 * @param language
	 *            the new {@link Language} to use
	 */
	public void pushLanguage(Language<C, T, R, D, L> language) {
		this.language.push(language);
	}
	
	/**
	 * Produces a shallow copy of this {@link LexerState} with the given {@link Language}. This method can be called from any {@link LexerAction}.<br>
	 * This method can only be used with custom descenders and even then only with the utmost care. Use of {@link #pushLanguage(Language)} and
	 * {@link #popLanguage()} is highly recommended.
	 * 
	 * @param language
	 *            the new {@link Language} to use
	 * @return a shallow copy of this {@link LexerState} with the given {@link Language}
	 * @see #pushLanguage(Language)
	 */
	public LexerState<C, T, R, D, L> setLanguage(Language<C, T, R, D, L> language) {
		return new LexerState<>(getInput(), getHead(), getDescender(), getLexer(), language);
	}
	
	/**
	 * @return a copy of the {@link LexerState} where only the {@link ConsCell ConsCells} are cloned.
	 */
	public LexerState<C, T, R, D, L> copy() {
		LexerState<C, T, R, D, L> copy = new LexerState<>(this, root, last);
		if (copy.root != null) {
			copy.root = copy.root.clone();
			copy.last = copy.root.getLast();
		}
		copy.lexer = copy.lexer.clone();
		copy.language = new Stack<>();
		for (int i = 0; i < language.size(); i++)
			copy.language.push(language.get(i).clone());
		return copy;
	}
}
