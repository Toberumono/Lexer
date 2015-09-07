package toberumono.lexer.genericBase;

@FunctionalInterface
interface AscentBlock<To extends GenericToken<Ty, To>, Ty extends GenericType, R extends GenericRule<To, Ty, R, D, L>, D extends GenericDescender<To, Ty, R, D, L>, L extends GenericLexer<To, Ty, R, D, L>>
		extends LogicBlock<To, Ty, R, D, L> {
		
}
