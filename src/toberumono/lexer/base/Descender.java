package toberumono.lexer.base;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A container that holds the information used to identify and act upon seeing a particular descent call.
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
public interface Descender<C extends ConsCell, T extends ConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>> {
	
	/**
	 * @return the {@link Pattern} that identifies tokens that open the {@link Descender}
	 */
	public Pattern getOpenPattern();
	
	/**
	 * @return the {@link Pattern} that identifies tokens that close the {@link Descender}
	 */
	public Pattern getClosePattern();
	
	/**
	 * @return the {@link LexerAction} that is performed when the {@link Descender} is closed
	 */
	public LexerAction<C, T, R, D, L, C> getCloseAction();
	
	/**
	 * @return the {@link LexerAction} that is performed when the {@link Descender} is opened
	 */
	public DescenderOpenAction<C, T, R, D, L, MatchResult> getOpenAction();
}
