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
	WHITESPACE(Pattern.compile("\\s+")),
	/**
	 * Ignores the contents of C-style comments.
	 * 
	 * @see #SINGLELINE_COMMENT
	 * @see #MULTILINE_COMMENT
	 */
	COMMENT(Pattern.compile("(//.*?" + System.lineSeparator() + "|/\\*.*?\\*/)", Pattern.DOTALL)),
	/**
	 * Ignores the contents of C-style single-line comments.
	 * @see #COMMENT
	 * @see #MULTILINE_COMMENT
	 */
	SINGLELINE_COMMENT(Pattern.compile("//.*?" + System.lineSeparator())),
	/**
	 * Ignores the contents of C-style multi-line comments.
	 * 
	 * @see #COMMENT
	 * @see #SINGLELINE_COMMENT
	 */
	MULTILINE_COMMENT(Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL));
	
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
