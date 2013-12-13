package lexer;

import lexer.abstractLexer.AbstractAction;

/**
 * Used to add a custom action in between finding the token in the lexer and storing it.</br> This uses the default
 * <tt>Token</tt> included in this library
 * 
 * @author Joshua Lipstone
 */
public interface Action<T> extends AbstractAction<Token, Type<T>, T> {}
