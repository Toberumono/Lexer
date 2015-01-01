package lipstone.joshua.lexer.abstractLexer;

import java.lang.reflect.InvocationTargetException;

/**
 * Base class for type flags used in {@link AbstractToken} and its subclasses.<br>
 * Commonly overridden methods are:
 * <ul>
 * <li>{@link #valueToString(Object)}</li>
 * <li>{@link #cloneValue(Object)}</li>
 * <li>{@link #compareValues(Object, Object)}</li>
 * </ul>
 * 
 * @author Joshua Lipstone
 */
public abstract class AbstractType {
	protected final String name, open, close;
	
	private final int hash;
	
	public AbstractType(String name, String open, String close) {
		this.name = name;
		this.open = open;
		this.close = close;
		hash = name.hashCode();
	}
	
	public AbstractType(String name) {
		this.name = name;
		open = null;
		close = null;
		hash = name.hashCode();
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
	 * By default this simply forwards to the value's {@link Object#toString()} method and, if this is a descender type,
	 * places the open and close symbols appropriately.
	 * 
	 * @param value
	 *            the value to convert to a {@link java.lang.String String}
	 * @return the value as a <tt>String</tt>
	 */
	public String valueToString(Object value) {
		return (open != null ? open + value.toString() + close : value.toString());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof AbstractType)
			return ((AbstractType) o).hash == hash;
		if (o instanceof String)
			return ((String) o).equals(name);
		return this == o;
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
	@SuppressWarnings("unchecked")
	public int compareValues(Object value1, Object value2) {
		return value1.getClass().isInstance(value2) && value1 instanceof Comparable ? ((Comparable<Object>) value1).compareTo(value2) : 0;
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
	public Object cloneValue(Object value) {
		try {
			return value instanceof Cloneable ? value.getClass().getMethod("clone").invoke(value) : value;
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			return value;
		}
	}
}
