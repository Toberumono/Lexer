package lexer.abstractLexer;

import java.util.regex.Matcher;

import lexer.Type;
import lexer.errors.LexerException;

/**
 * A Descender for the <tt>Lexer</tt>
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the subclass of {@link lexer.abstractLexer.AbstractToken Token} (including {@link lexer.abstractLexer.AbstractToken Token}) to use
 * @param <U>
 *            the subclass of {@link lexer.abstractLexer.AbstractToken Token} (including {@link lexer.Type Type}) to use
 * @param <V>
 *            the subclass of {@link lexer.abstractLexer.AbstractAction AbstractAction} to use.
 */
public abstract class AbstractDescender<T extends AbstractToken<? extends Type<?>, T>, U extends Type<T>, V extends AbstractAction<T, U, T, W>, W extends AbstractLexer<T, ? extends Type<?>, ? extends AbstractRule<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, W>, ?, W>, ? extends AbstractDescender<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, W>, W>, W>> {
	protected final String open, close;
	protected final U type;
	protected final V action;
	
	public AbstractDescender(String open, String close, U type, V action) {
		this.open = open;
		this.close = close;
		type.setOpenClose(open, close);
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Descender</tt>
	 * 
	 * @param match
	 * @param lexer
	 * @return the resulting value for a representative <tt>Token</tt>
	 * @throws LexerException
	 */
	protected abstract T apply(Matcher match, W lexer) throws LexerException;
	
	public U getType() {
		return type;
	}
	
}
