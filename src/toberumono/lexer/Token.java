package toberumono.lexer;

import toberumono.lexer.genericBase.GenericToken;

/**
 * An implementation of {@link GenericToken}
 * 
 * @author Toberumono
 */
public class Token extends GenericToken<Type, Token> {
	
	/**
	 * Constructs a {@link Token} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 */
	public Token(Object car, Type carType, Object cdr, Type cdrType) {
		super(car, carType, cdr, cdrType, Token::new, Type.TOKEN, Type.EMPTY);
	}
	
	/**
	 * Constructs a {@link Token} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 */
	public Token(Object car, Type carType) {
		super(car, carType, Token::new, Type.TOKEN, Type.EMPTY);
	}
	
	/**
	 * Constructs an empty {@link Token}.
	 */
	public Token() {
		super(Token::new, Type.TOKEN, Type.EMPTY);
	}
}
