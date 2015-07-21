package toberumono.lexer;

import toberumono.lexer.genericBase.GenericToken;

public class Token extends GenericToken<Type, Token> {
	
	public Token(Object car, Type carType, Object cdr, Type cdrType) {
		super(car, carType, cdr, cdrType, Token::new, Type.TOKEN, Type.EMPTY);
	}
	
	public Token(Object car, Type carType) {
		super(car, carType, Token::new, Type.TOKEN, Type.EMPTY);
	}
	
	public Token() {
		super(Token::new, Type.TOKEN, Type.EMPTY);
	}
}
