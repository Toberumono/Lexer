package toberumono.lexer.util;

import java.util.regex.Pattern;

import toberumono.lexer.genericBase.GenericLexer;
import toberumono.lexer.genericBase.IgnorePattern;
import toberumono.structures.sexps.ConsCellConstructor;
import toberumono.structures.sexps.GenericConsType;

/**
 * A few patterns that are commonly included as spacing between tokens and should therefore be ignored.<br>
 * Mainly used in the {@link GenericLexer GenericLexer's} constructor.
 * 
 * @author Toberumono
 * @see GenericLexer#GenericLexer(ConsCellConstructor, GenericConsType, IgnorePattern...)
 * @see CommentPatterns
 */
public enum DefaultIgnorePatterns implements IgnorePattern {
	/**
	 * Ignores spaces {@code Pattern.compile(" +")}
	 */
	SPACES(Pattern.compile(" +")),
	/**
	 * Ignores newlines {@code Pattern.compile(System.lineSeparator(), Pattern.LITERAL)}
	 */
	NEW_LINES(Pattern.compile(System.lineSeparator(), Pattern.LITERAL)),
	/**
	 * Ignores all whitespace characters {@code Pattern.compile("\\s+")}
	 */
	WHITESPACE(Pattern.compile("\\s+"));
	
	private Pattern pattern;
	
	private DefaultIgnorePatterns(Pattern pattern) {
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
