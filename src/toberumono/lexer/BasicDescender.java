package toberumono.lexer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import toberumono.lexer.base.AbstractDescender;
import toberumono.lexer.base.LexerAction;
import toberumono.lexer.util.DefaultPattern;
import toberumono.lexer.base.DescenderOpenAction;
import toberumono.lexer.base.Lexer;
import toberumono.structures.sexpressions.ConsCell;
import toberumono.structures.sexpressions.ConsType;

/**
 * A Descender for the {@link Lexer} that uses the provided {@code ConsCell} class
 * 
 * @author Toberumono
 */
public class BasicDescender extends AbstractDescender<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer> {
	
	/**
	 * Constructs a {@link BasicDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param type
	 *            the {@link ConsType type} to be associated with the {@link BasicDescender}
	 */
	public BasicDescender(String open, String close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a {@link BasicDescender} that takes no action when the open token is matched with the given open and close
	 * tokens and close action.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(String open, String close, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a {@link BasicDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(String open, String close, DescenderOpenAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> openAction,
			LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link BasicDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} open tokens
	 * @param close
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} close tokens
	 * @param type
	 *            the {@link ConsType type} to be associated with the {@link BasicDescender}
	 */
	public BasicDescender(Pattern open, Pattern close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a {@link BasicDescender} that takes no action when the open token is matched with the given open and close
	 * tokens and close action.
	 * 
	 * @param open
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} open tokens
	 * @param close
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} close tokens
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(Pattern open, Pattern close, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a {@link BasicDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} open tokens
	 * @param close
	 *            the {@link Pattern} that describes the {@link BasicDescender BasicDescender's} close tokens
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(Pattern open, Pattern close, DescenderOpenAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> openAction,
			LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
	
	/**
	 * Constructs a {@link BasicDescender} that corresponds to the given type with the given open and close tokens.
	 * 
	 * @param open
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param close
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param type
	 *            the {@link ConsType type} to be associated with the {@link BasicDescender}
	 */
	public BasicDescender(DefaultPattern open, DefaultPattern close, ConsType type) {
		super(open, close, type);
	}
	
	/**
	 * Constructs a {@link BasicDescender} that takes no action when the open token is matched with the given open and close
	 * tokens and close action.
	 * 
	 * @param open
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param close
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(DefaultPattern open, DefaultPattern close, LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, closeAction);
	}
	
	/**
	 * Constructs a {@link BasicDescender} with the given open and close tokens and open and close actions.
	 * 
	 * @param open
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param close
	 *            the {@link DefaultPattern} containing the regex {@link Pattern} that describes the {@link BasicDescender
	 *            BasicDescender's} open tokens
	 * @param openAction
	 *            the {@link DescenderOpenAction} to perform when the open token is encountered (prior to descending)
	 * @param closeAction
	 *            the {@link LexerAction} to perform when the close token is encountered (prior to ascending)
	 */
	public BasicDescender(DefaultPattern open, DefaultPattern close, DescenderOpenAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, MatchResult> openAction,
			LexerAction<ConsCell, ConsType, BasicRule, BasicDescender, BasicLexer, ConsCell> closeAction) {
		super(open, close, openAction, closeAction);
	}
}
