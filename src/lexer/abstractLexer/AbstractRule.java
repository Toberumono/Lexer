package lexer.abstractLexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Type;
import lexer.errors.LexerException;

public abstract class AbstractRule<T extends AbstractToken<? extends Type<?>, T>, U extends Type<W>, W, L extends AbstractLexer<T, ? extends Type<?>, ? extends AbstractRule<T, ? extends Type<?>, ?, L>, ? extends AbstractDescender<T, ? extends Type<?>, L>, L>> {
	public final Pattern pattern;
	public final U type;
	
	/**
	 * Constructs a new <tt>Rule</tt> with the given data
	 * 
	 * @param pattern
	 *            a regex <tt>String</tt> that contains the <tt>Pattern</tt> for this <tt>Rule</tt> to use.
	 * @param type
	 *            the the type for <tt>Token</tt>s matched by this rule
	 */
	public AbstractRule(String pattern, U type) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern) : Pattern.compile("\\G" + pattern);
		this.type = type;
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
	 */
	public AbstractRule(String pattern, int flags, U type) {
		this.pattern = pattern.startsWith("\\G") ? Pattern.compile(pattern, flags) : Pattern.compile("\\G" + pattern, flags);
		this.type = type;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Rule</tt>
	 * 
	 * @param match
	 *            the {@link java.lang.String String} that was matched
	 * @param lexer
	 *            the <tt>Lexer</tt> that matched the provided {@link java.lang.String String}
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException
	 */
	protected T apply(Matcher match, L lexer) throws LexerException {
		return ((TokenConstructor<U, T>) lexer.getTokenConstructor()).makeNewToken(match.group(), type);
	}
}
