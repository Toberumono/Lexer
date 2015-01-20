package lipstone.joshua.lexer.abstractLexer;

/**
 * Represents the action to take upon seeing a particular descent-start token.
 * 
 * @author Joshua Lipstone
 * @param <To>
 *            the implementation of {@link AbstractToken} to use
 * @param <Ty>
 *            the implementation of {@link GenericType} to use
 * @param <L>
 *            the implementation of {@link AbstractLexer} to use
 */
public class AbstractDescender<To extends AbstractToken<Ty, To>, Ty extends GenericType, L extends AbstractLexer<To, Ty, ?, ?, L>> {
	protected final String open, close;
	protected final LexerAction<To, To, L> closeAction;
	protected final DescenderAction<L> openAction;
	
	public AbstractDescender(String open, String close, Ty type) {
		this(open, close, (lexer) -> {}, (match, lexer) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(match, type, null, lexer.emptyType));
	}
	
	public AbstractDescender(String open, String close, DescenderAction<L> openAction, LexerAction<To, To, L> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
