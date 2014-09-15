package lexer;

import java.lang.reflect.InvocationTargetException;

import lexer.abstractLexer.AbstractToken;

@SuppressWarnings("rawtypes")
public class Type<T> {
	
	/**
	 * A pre-created type that <i>must</i> be used to denote empty values (e.g. the end of a list)
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
	 * A pre-created <tt>Type</tt> that flags the value as an instance of <tt>AbstractToken</tt>
	 */
	public static final Type<? extends AbstractToken> TOKEN = new Type<AbstractToken>("Token") {
		
		@Override
		public AbstractToken cloneValue(Object value) {
			return ((AbstractToken) value).clone();
		}
	};
	
	protected final String name;
	protected String open, close;
	
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
	
	public final void setOpenClose(String open, String close) {
		this.open = open;
		this.close = close;
	}
	
	public final String getOpen() {
		return open;
	}
	
	public final String getClose() {
		return close;
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
	 *            the value to convert to a {@link java.lang.String String}
	 * @return the value as a <tt>String</tt>
	 */
	public String valueToString(Object value) {
		return (open != null ? open + ((T) value).toString() + close : ((T) value).toString()) + " ";
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
	 * By default this forwards to the compareTo method of whatever object this <tt>Type</tt> denotes.<br>
	 * If the value type does not implement comparable, it returns 0.
	 * 
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @return the value of the implied value type's compareTo method if it implements <tt>Comparable</tt> otherwise 0.
	 */
	public int compareValues(Object value1, Object value2) {
		return value1.getClass().isInstance(value2) && value1 instanceof Comparable ? ((Comparable<T>) value1).compareTo((T) value2) : 0;
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
	public T cloneValue(Object value) {
		try {
			return value instanceof Cloneable ? (T) value.getClass().getMethod("clone").invoke(value) : (T) value;
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			return (T) value;
		}
	}
}