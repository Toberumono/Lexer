package toberumono.lexer.base;

import java.util.regex.Matcher;

import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

@FunctionalInterface
interface AscentBlock<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		extends Action<C, T, R, D, L, Matcher> {
		
}
