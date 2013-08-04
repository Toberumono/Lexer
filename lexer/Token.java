package lexer;

public class Token {
	private Type type;
	private Object value;
	
	public Token(Object value, Type type) {
		this.type = type;
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return type.valueToString(value);
	}
}
