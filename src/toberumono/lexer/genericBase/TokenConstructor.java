package toberumono.lexer.genericBase;

/**
 * Represents the basic constructor for an implementation of {@link GenericToken}.
 * 
 * @author Toberumono
 * @param <Ty>
 *            the implementation of {@link GenericType} in use
 * @param <To>
 *            the implementation of {@link GenericToken} in use
 */
@FunctionalInterface
public interface TokenConstructor<Ty extends GenericType, To extends GenericToken<Ty, To>> {
	public To makeNewToken(Object car, Ty carType, Object cdr, Ty cdrType);
}
