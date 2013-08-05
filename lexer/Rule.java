package lexer;

import java.util.regex.Pattern;

public final class Rule<T> {
	final Pattern pattern;
	private final Type<T> type;
	private final Action<T> action;
	
	public Rule(Pattern pattern, Type<T> type, Action<T> action) {
		this.pattern = pattern;
		this.type = type;
		this.action = action;
	}
	
	/**
	 * Apply the <tt>Action</tt> associated with this <tt>Rule</tt>
	 * @param match
	 * @param lexer
	 * @return the resulting value for a representative <tt>Token</tt>
	 */
	final T apply(String match, Lexer lexer) {
		return action == null ? (T) match : action.action(match, lexer);
	}
	
	public final Type<T> getType() {
		return type;
	}
}
