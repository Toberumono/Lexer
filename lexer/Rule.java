package lexer;

import java.util.regex.Pattern;

public class Rule {
	final Pattern pattern;
	private final Type type;
	private final Action action;
	
	public Rule(Pattern pattern, Type type, Action action) {
		this.pattern = pattern;
		this.type = type;
		this.action = action;
	}
	
	Token apply(String match, Lexer lexer) {
		return new Token(action == null ? match : action.action(match, lexer), type);
	}
}
