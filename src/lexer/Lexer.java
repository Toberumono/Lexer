package lexer;

import java.util.regex.Matcher;

import lexer.abstractLexer.AbstractLexer;
import lexer.errors.LexerException;

public class Lexer extends AbstractLexer<Token, Type<?>, Rule<?>, Descender> {
	
	/**
	 * Basic constructor for a Lexer
	 */
	public Lexer() {
		super();
	}

	@Override
	public Token makeNewToken() {
		return new Token();
	}

	@Override
	protected Token descend(Descender d, Matcher m) throws LexerException {
		return d.apply(m, this);
	}

	@Override
	protected Token hit(Rule<?> r, Matcher m) throws LexerException {
		return r.apply(m, this);
	}
}
