package lipstone.joshua.lexer.abstractLexer;

@FunctionalInterface
public interface TokenConstructor<Ty extends AbstractType, To extends AbstractToken<Ty, To>> {
	public To makeNewToken(Object car, Ty carType, Object cdr, Ty cdrType);
}
