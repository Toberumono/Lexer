package lexer.abstractLexer;

import lexer.Type;
import lexer.errors.LexerException;

/**
 * A Descender for the <tt>Lexer</tt>
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the subclass of {@link lexer.abstractLexer.AbstractToken Token} (including
 *            {@link lexer.abstractLexer.AbstractToken Token}) to use
 * @param <U>
 *            the subclass of {@link lexer.abstractLexer.AbstractToken Token} (including {@link lexer.Type Type}) to use
 */
public abstract class AbstractDescender<T extends AbstractToken<? extends Type<?>, T>, U extends Type<?>, L extends AbstractLexer<T, ? extends Type<?>, ? extends AbstractRule<T, ? extends Type<?>, ?, L>, ? extends AbstractDescender<T, ? extends Type<?>, L>, L>> {
	protected final String open, close;
	protected final U type;
	
	public AbstractDescender(String open, String close, U type) {
		this.open = open;
		this.close = close;
		type.setOpenClose(open, close);
		this.type = type;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Descender</tt>
	 * 
	 * @param match
	 *            the {@link java.lang.String String} that was matched
	 * @param lexer
	 *            the <tt>Lexer</tt> that matched the provided {@link java.lang.String String}
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException
	 */
	protected T apply(String match, L lexer) throws LexerException {
		return ((TokenConstructor<U, T>) lexer.getTokenConstructor()).makeNewToken(lexer.lex(match), type);
	}
	
	public U getType() {
		return type;
	}
	
}
