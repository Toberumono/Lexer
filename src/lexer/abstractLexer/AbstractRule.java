package lexer.abstractLexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Type;
import lexer.errors.LexerException;

public abstract class AbstractRule<T extends AbstractToken<? extends Type<?>, T>, U extends Type<W>, V extends AbstractAction<T, U, W, L>, W, L extends AbstractLexer<T, ? extends Type<?>, ? extends AbstractRule<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, L>, ?, L>, ? extends AbstractDescender<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, L>, L>, L>> {
	protected final Pattern pattern;
	protected final U type;
	protected final V action;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 * @param action
	 *            the <tt>Action</tt> to take on <tt>Token</tt>s matched by this rule
	 */
	public AbstractRule(String pattern, U type, V action) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern) : Pattern.compile("\\G" + pattern);
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param flags
	 *            the regex flags defined in {@link java.util.regex.Pattern Pattern}
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 * @param action
	 *            the <tt>Action</tt> to take on <tt>Token</tt>s matched by this rule
	 */
	public AbstractRule(String pattern, int flags, U type, V action) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern, flags) : Pattern.compile("\\G" + pattern, flags);
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Rule</tt>
	 * 
	 * @param match
	 * @param lexer
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException
	 */
	protected abstract T apply(Matcher match, L lexer) throws LexerException;
	
	public U getType() {
		return type;
	}
	
	/**
	 * @return the pattern that this <tt>Rule</tt> matches
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
}
