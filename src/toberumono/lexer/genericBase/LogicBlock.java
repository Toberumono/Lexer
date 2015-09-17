package toberumono.lexer.genericBase;

import java.util.regex.Matcher;

import toberumono.lexer.errors.LexerException;
import toberumono.structures.sexps.GenericConsCell;
import toberumono.structures.sexps.GenericConsType;

@FunctionalInterface
public interface LogicBlock<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>> {
	public C handle(L lexer, LexerState<C, T, R, D, L> state, Matcher match) throws LexerException;
}
