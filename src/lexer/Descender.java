package lexer;

import java.util.regex.Matcher;

import lexer.abstractLexer.AbstractDescender;
import lexer.errors.LexerException;

/**
 * A Descender for the <tt>Lexer</tt> that uses the provided <tt>Token</tt> class
 * 
 * @author Joshua Lipstone
 */
public final class Descender extends AbstractDescender<Token, Type<Token>, Action<Token>, Lexer> {
	
	public Descender(String open, String close, Type<Token> type, Action<Token> action) {
		super(open, close, type, action);
	}

	@Override
	protected Token apply(Matcher match, Lexer lexer) throws LexerException {
		return action == null ? new Token(lexer.lex(match.group()), type) : action.action(match, lexer, type);
	}
}
