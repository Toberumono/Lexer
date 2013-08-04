package lexer;

public class Descender {
	final String open, close;
	private final Type type;
	private final Action action;
	
	public Descender(String open, String close, Type type, Action action) {
		this.open = open;
		this.close = close;
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Descender</tt>
	 * @param match
	 * @param lexer
	 * @return the resulting <tt>Token</tt>
	 */
	Token apply(String match, Lexer lexer) {
		return new Token(action == null ? lexer.lex(match) : action.action(match, lexer), type);
	}
}
