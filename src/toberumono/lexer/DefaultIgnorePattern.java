package toberumono.lexer;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericLexer;
import toberumono.lexer.genericBase.GenericType;
import toberumono.lexer.genericBase.TokenConstructor;

/**
 * A few patterns that are commonly included as spacing between tokens and should therefore be ignored.<br>
 * Mainly used in the {@link GenericLexer GenericLexer's} constructor.
 * 
 * @author Toberumono
 * @see GenericLexer#GenericLexer(TokenConstructor, GenericType, IgnorePattern...)
 */
public enum DefaultIgnorePattern implements IgnorePattern {
	SPACES(Pattern.compile(" +")),
	NEW_LINES(Pattern.compile(System.lineSeparator(), Pattern.LITERAL)),
	WHITESPACE(Pattern.compile("\\s+"));
	
	private Pattern pattern;
	
	private DefaultIgnorePattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String getName() {
		return this.toString();
	}
}
