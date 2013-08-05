package lexer;

public class Type<T> {
	
	public static final Type<Object> EMPTY = new Type<Object>("Empty", false) {
		@Override
		public String valueToString(Object value) {
			return "";
		}
		
		@Override
		public int compareValues(Object value1, Object value2) {
			return 0;
		}
	};
	
	public static final Type<Token> TOKEN = new Type<Token>("Token", true) {
		@Override
		public String valueToString(Token value) {
			Token current = (Token) value;
			String output = "(";
			do {
				output = output + current.toString() + " ";
			} while ((current = current.getNextToken()).isNull());
			if (output.length() > 1)
				output = output.substring(0, output.length() - 1);
			output = output + ")";
			return output;
		}
		
		@Override
		public int compareValues(Token value1, Token value2) {
			if (value1 instanceof Token)
				if (value2 instanceof Token)
					return ((Token) value1).compareTo((Token) value2);
				else
					return 1;
			else if (value2 instanceof Token)
				return -1;
			return 0;
		}
	};
	
	private final String name;
	private final boolean descenderType;
	
	public Type(String name, boolean descenderType) {
		this.name = name;
		this.descenderType = descenderType;
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
		return descenderType;
	}
	
	/**
	 * By default this simply forwards to the value's toString() method. However, this can be overridden for
	 * implementation-specific methods.
	 * 
	 * @param value
	 * @return the value as a <tt>String</tt>
	 */
	public String valueToString(T value) {
		return value.toString();
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
	public int compareValues(T value1, T value2) {
		if (value1 instanceof Comparable)
			return ((Comparable<T>) value1).compareTo(value2);
		return 0;
	}
}