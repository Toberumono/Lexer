package lipstone.joshua.lexer.abstractLexer;

/**
 * Represents an action to perform when the open token for a {@link AbstractDescender} is found.
 * 
 * @author Joshua Lipstone
 * @param <L>
 *            the subclass of {@link AbstractLexer} to use
 */
@FunctionalInterface
public interface DescenderAction<L> {
	public void perform(L lexer) throws Exception;
}
