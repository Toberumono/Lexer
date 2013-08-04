package lexer;

public interface Action {
	
	public Object action(String input, Lexer lexer);
}
