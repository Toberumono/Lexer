package lexer;

import lexer.abstractLexer.AbstractToken;

public class Token extends AbstractToken<Type<?>, Token> {
	
	public Token(Object car, Type<?> carType, Object cdr, Type<?> cdrType) {
		super(car, carType, cdr, cdrType);
	}
	
	public Token(Object car, Type<?> carType) {
		super(car, carType);
	}
	
	public Token() {
		super();
	}
	
	@Override
	public Token singular() {
		return new Token(carType.clone(car), carType);
	}
	
	@Override
	public Token makeNewToken() {
		return new Token();
	}
	
	@Override
	protected Token clone(Token previous) {
		Token clone = new Token(carType.clone(car), carType, cdrType.clone(cdr), cdrType);
		clone.previous = previous;
		return clone;
	}

	@Override
	protected Type<?> getTokenType() {
		return Type.TOKEN;
	}
}
