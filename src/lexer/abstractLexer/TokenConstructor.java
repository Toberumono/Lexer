package lexer.abstractLexer;

import lexer.Type;

@FunctionalInterface
public interface TokenConstructor<V extends AbstractToken<? extends Type<?>, V>> {
	public V makeNewToken(Object car, Type<?> carType, Object cdr, Type<?> cdrType);
	
	default V makeNewToken() {
		return makeNewToken(null, Type.EMPTY, null, Type.EMPTY);
	}
	
	default V makeNewToken(Object car, Type<?> carType) {
		return makeNewToken(car, carType, null, Type.EMPTY);
	}
}
