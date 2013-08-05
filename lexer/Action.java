package lexer;

public interface Action<T> {
	
	public T action(String input, Lexer lexer);
}
