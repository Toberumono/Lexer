package toberumono.lexer.genericBase;

/**
 * Represents the action to take upon seeing a particular descent-start token.
 * 
 * @author Toberumono
 * @param <To>
 *            the implementation of {@link GenericToken} to use
 * @param <Ty>
 *            the implementation of {@link GenericType} to use
 * @param <L>
 *            the implementation of {@link GenericLexer} to use
 */
public class GenericDescender<To extends GenericToken<Ty, To>, Ty extends GenericType, L extends GenericLexer<To, Ty, ?, ?, L>> {
	protected final String open, close;
	protected final GenericAction<To, To, L> closeAction;
	protected final DescenderAction<L> openAction;
	
	public GenericDescender(String open, String close, Ty type) {
		this(open, close, (lexer) -> {}, (match, lexer) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(match, type, null, lexer.emptyType));
	}
	
	public GenericDescender(String open, String close, DescenderAction<L> openAction, GenericAction<To, To, L> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
