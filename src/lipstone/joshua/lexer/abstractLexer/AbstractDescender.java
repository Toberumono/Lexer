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
public abstract class AbstractDescender<To extends AbstractToken<Ty, To>, Ty extends AbstractType<?, Ty>, L extends AbstractLexer<To, Ty, ?, ?, L>> {
	protected final String open, close;
	protected final LexerAction<To, String, L> action;
	
	public AbstractDescender(String open, String close, Ty type) {
		this(open, close, (match, lexer) -> {
			return ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).makeNewToken(lexer.lex(match), type, null, lexer.emptyType);
		});
	}
	
	public AbstractDescender(String open, String close, LexerAction<To, String, L> action) {
		this.open = open;
		this.close = close;
		this.action = action;
	}
}
