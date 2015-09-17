package toberumono.lexer.genericBase;

import java.util.regex.Matcher;

import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

@FunctionalInterface
interface AscentBlock<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>>
		extends GenericAction<C, T, R, D, L, Matcher> {
		
}
