package lipstone.joshua.lexer.abstractLexer;

/**
 * Represents the basic constructor for an implementation of {@link AbstractToken}.
 * 
 * @author Joshua Lipstone
 * @param <Ty>
 *            the implementation of {@link GenericType} in use
 * @param <To>
 *            the implementation of {@link AbstractToken} in use
 */
@FunctionalInterface
public interface TokenConstructor<Ty extends GenericType, To extends AbstractToken<Ty, To>> {
	public To makeNewToken(Object car, Ty carType, Object cdr, Ty cdrType);
}
