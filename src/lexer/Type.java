package lexer;

import java.lang.reflect.InvocationTargetException;

public class Type<T> {
	
	/**
	 * A pre-created type that <i>must</i> be used to denote empty tokens (e.g. the end of a list)
	 */
	public static final Type<Object> EMPTY = new Type<Object>("Empty") {
		@Override
		public String valueToString(Object value) {
			return "";
		}
		
		@Override
		public int compareValues(Object value1, Object value2) {
			return 0;
		}
	};
	
	/**
	 * A pre-created type that flags the <tt>Token</tt> as a descender point (e.g. parentheses)
	 */
	public static final Type<Token> TOKEN = new TokenType("Token", null, null);
	
	protected final String name, open, close;
	
	public Type(String name, String open, String close) {
		this.name = name;
		this.open = open;
		this.close = close;
	}
	
	public Type(String name) {
		this.name = name;
		open = null;
		close = null;
	}
	
	public Type(Type<T> basis, String open, String close) {
		name = basis.name;
		this.open = open;
		this.close = close;
	}
	
	/**
	 * @return the name of this Type
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return whether this <tt>Type</tt> indicates a descender (its associated field is a subclass of Token)
	 */
	public final boolean marksDescender() {
		return open != null;
	}
	
	/**
	 * By default this simply forwards to the value's toString() method. However, this can be overridden for
	 * implementation-specific methods.
	 * 
	 * @param value
	 * @return the value as a <tt>String</tt>
	 */
	public String valueToString(T value) {
		return (open != null ? open + value.toString() + close : value.toString()) + " ";
	}
	
	String vts(Object value) {
		return valueToString((T) value);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o instanceof Type)
			return ((Type<?>) o).name.equals(name);
		if (o instanceof String)
			return ((String) o).equals(name);
		return false;
	}
	
	/**
	 * By default this forwards to the compareTo method of whatever object this <tt>Type</tt> denotes.</br> If the value type
	 * does not implement comparable, it returns 0.
	 * 
	 * @param value1
	 * @param value2
	 * @return the value of the implied value type's compareTo method if it implements <tt>Comparable</tt> otherwise 0.
	 */
	public int compareValues(Object value1, Object value2) {
		if (value1.getClass().isInstance(value2) && value1 instanceof Comparable)
			return ((Comparable<T>) value1).compareTo((T) value2);
		return 0;
	}
	
	/**
	 * By default this just returns the object, which works for immutable objects like <tt>String</tt>, <tt>Integer</tt>,
	 * <tt>Double</tt>, etc or objects that implement the <tt>Cloneable</tt> interface. However, types that use any other
	 * object type should override this method.
	 * 
	 * @param value
	 *            the value to clone
	 * @return a clone of the passed object
	 */
	public T clone(Object value) {
		try {
			return value instanceof Cloneable ? (T) value.getClass().getMethod("clone").invoke(value) : (T) value;
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			return (T) value;
		}
	}
}

class TokenType extends Type<Token> {
	
	public TokenType(String name, String open, String close) {
		super(name, open, close);
	}
	
	@Override
	public int compareValues(Object value1, Object value2) {
		if (value1 instanceof Token)
			if (value2 instanceof Token)
				return ((Token) value1).compareTo((Token) value2);
			else
				return 1;
		else if (value2 instanceof Token)
			return -1;
		return 0;
	}
	
	@Override
	public Token clone(Object value) {
		if (value instanceof Token)
			return ((Token) value).clone();
		return super.clone(value);
	}
}