package toberumono.lexer.genericBase;

import java.util.regex.Matcher;

import toberumono.lexer.errors.LexerException;

interface LogicBlock<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>> {
	public To handle(L lexer, LexerState<To, Ty, R, D, L> state, Matcher match) throws LexerException;
}
