package toberumono.lexer.genericBase;

import toberumono.structures.sexps.GenericConsCell;
import toberumono.structures.sexps.GenericConsType;

@FunctionalInterface
interface AscentBlock<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends GenericRule<C, T, R, D, L>, D extends GenericDescender<C, T, R, D, L>, L extends GenericLexer<C, T, R, D, L>>
		extends LogicBlock<C, T, R, D, L> {
		
}
