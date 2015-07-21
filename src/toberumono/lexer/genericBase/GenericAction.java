package toberumono.lexer.genericBase;

/**
 * Represents an action to apply to a matched part of an input.
 * 
 * @author Toberumono
 * @param <To>
 *            the subclass of {@link GenericToken} to use
 * @param <M>
 *            the type appropriate for the match data
 * @param <L>
 *            the subclass of {@link GenericLexer} to use
 */
@FunctionalInterface
public interface GenericAction<To, M, L> {
	public To perform(M match, L lexer) throws Exception;
}
