package toberumono.lexer.genericBase;

import java.util.regex.Pattern;

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
public class GenericDescender<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>> {
	protected final Pattern open, close;
	protected final GenericAction<To, Ty, R, D, L, To> closeAction;
	protected final DescenderAction<L> openAction;
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(String open, String close, Ty type) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), (lexer, state) -> {} ,
				(lexer, state, match) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(String open, String close, DescenderAction<L> openAction, GenericAction<To, Ty, R, D, L, To> closeAction) {
		this(Pattern.compile(open, Pattern.LITERAL), Pattern.compile(close, Pattern.LITERAL), openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link GenericDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link GenericType type} to be associated with the {@link GenericDescender}
	 */
	public GenericDescender(Pattern open, Pattern close, Ty type) {
		this(open, close, (lexer, state) -> {} , (lexer, state, match) -> ((TokenConstructor<Ty, To>) lexer.getTokenConstructor()).construct(match, type, null, lexer.emptyType));
	}
	
	/**
	 * Constructs a {@link GenericDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link GenericAction} to perform when the close token is encountered (prior to ascending)
	 */
	public GenericDescender(Pattern open, Pattern close, DescenderAction<L> openAction, GenericAction<To, Ty, R, D, L, To> closeAction) {
		this.open = open;
		this.close = close;
		this.openAction = openAction;
		this.closeAction = closeAction;
	}
}
