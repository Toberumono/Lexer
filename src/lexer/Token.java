package lexer;

import lexer.abstractLexer.AbstractToken;

public class Token extends AbstractToken {
	
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
	public AbstractToken singular() {
		return new Token(carType.clone(car), carType);
	}
	
	@Override
	public AbstractToken makeNewToken() {
		return new Token();
	}
	
	@Override
	protected AbstractToken clone(AbstractToken previous) {
		Token clone = new Token(carType.clone(car), carType, cdrType.clone(cdr), cdrType);
		clone.previous = previous;
		return clone;
	}
}
