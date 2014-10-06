package lexer.abstractLexer;

import lexer.Type;

@FunctionalInterface
public interface TokenConstructor<T extends Type<?>, V extends AbstractToken<? extends Type<?>, V>> {
	public V makeNewToken(Object car, T carType, Object cdr, T cdrType);
	
	default V makeNewToken() {
		return makeNewToken(null, (T) T.EMPTY, null, (T) T.EMPTY);
	}
	
	default V makeNewToken(Object car, T carType) {
		return makeNewToken(car, carType, null, (T) T.EMPTY);
	}
}
