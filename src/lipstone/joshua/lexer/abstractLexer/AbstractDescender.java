package lipstone.joshua.lexer.abstractLexer;

/**
 * Represents the action to take upon seeing a particular descent-start token.
 * 
 * @author Joshua Lipstone
 * @param <To>
 *            the subclass of {@link AbstractToken} to use
 * @param <Ty>
 *            the subclass of {@link AbstractType} to use
 * @param <L>
 *            the subclass of {@link AbstractLexer} to use
 */
public abstract class AbstractDescender<To extends AbstractToken<Ty, To>, Ty extends AbstractType, L extends AbstractLexer<To, Ty, ?, ?, L>> {
	protected final String open, close;
	protected final LexerAction<To, To, L> closeAction;
	protected final DescenderAction<L> openAction;
	
	public AbstractDescender(String open, String close, Ty type) {
		this(open, close, (lexer) -> {}, (match, lexer) -> {
			return ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(match, type, null, lexer.emptyType);
		});
	}
	
	public AbstractDescender(String open, String close, DescenderAction<L> openAction, LexerAction<To, To, L> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
