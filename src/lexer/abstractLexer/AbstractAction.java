package lexer.abstractLexer;

import java.util.regex.Matcher;

import lexer.Type;
import lexer.errors.LexerException;

/**
 * Used to add a custom action in between finding the token in the lexer and storing it.
 * 
 * @author Joshua Lipstone
 */
public interface AbstractAction<T extends AbstractToken<? extends Type<?>, T>, U extends Type<V>, V, W extends AbstractLexer<T, ? extends Type<?>, ? extends AbstractRule<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, W>, ?, W>, ? extends AbstractDescender<T, ? extends Type<?>, ? extends AbstractAction<T, ? extends Type<?>, ?, W>, W>, W>> {
	
	/**
	 * @param match
	 *            the token in the <tt>String</tt>
	 * @param lexer
	 *            the lexer that found the token
	 * @param type
	 *            the type of the output token
	 * @return the result of applying the Action on the token
	 */
	public T action(Matcher match, W lexer, U type) throws LexerException;
}
