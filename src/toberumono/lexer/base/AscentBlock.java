package toberumono.lexer.base;

import java.util.regex.Matcher;

import toberumono.structures.sexpressions.generic.GenericConsCell;
import toberumono.structures.sexpressions.generic.GenericConsType;

/**
 * This is a simple marker interface that is used internally by {@link AbstractLanguage} and {@link AbstractLexer} to
 * indicate that a given action decreases the depth of the following tokens by 1.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} to be used
 * @param <T>
 *            the implementation of {@link GenericConsType} to be used
 * @param <R>
 *            the implementation of {@link Rule} to be used
 * @param <D>
 *            the implementation of {@link Descender} to be used
 * @param <L>
 *            the implementation of {@link Lexer} to be used
 */
@FunctionalInterface
public interface AscentBlock<C extends GenericConsCell<T, C>, T extends GenericConsType, R extends Rule<C, T, R, D, L>, D extends Descender<C, T, R, D, L>, L extends Lexer<C, T, R, D, L>>
		extends LexerAction<C, T, R, D, L, Matcher> {}
