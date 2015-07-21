package toberumono.lexer.genericBase;

/**
 * Represents an action to perform when the open token for a {@link GenericDescender} is found.
 * 
 * @author Toberumono
 * @param <L>
 *            the subclass of {@link GenericLexer} to use
 */
@FunctionalInterface
public interface DescenderAction<L> {
	public void perform(L lexer) throws Exception;
}
