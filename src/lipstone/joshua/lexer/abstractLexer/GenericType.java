package lipstone.joshua.lexer.abstractLexer;

/**
 * Base interface for type flags used in {@link AbstractToken} and its subclasses.<br>
 * Commonly overridden methods are:
 * <ul>
 * <li>{@link #valueToString(Object)}</li>
 * <li>{@link #cloneValue(Object)}</li>
 * <li>{@link #compareValues(Object, Object)}</li>
 * </ul>
 * 
 * @author Joshua Lipstone
 */
public interface GenericType {
	
	/**
	 * @return the open symbol for this type if it indicates a descender, otherwise null
	 */
	public String getOpen();
	
	/**
	 * @return the close symbol for this type if it indicates a descender, otherwise null
	 */
	public String getClose();
	
	/**
	 * @return the name of this {@link GenericType type}
	 */
	public String getName();
	
	/**
	 * @return whether this <tt>Type</tt> indicates a descender (its associated field is a subclass of {@link AbstractToken})
	 */
	public boolean marksDescender();
	
	/**
	 * By default this simply forwards to the value's {@link Object#toString()} method and, if this is a descender type,
	 * places the open and close symbols appropriately.
	 * 
	 * @param value
	 *            the value to convert to a {@link java.lang.String String}
	 * @return the value as a <tt>String</tt>
	 */
	public default String valueToString(Object value) {
		return (getOpen() != null ? getOpen() + value.toString() + getClose() : value.toString());
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
	public default int compareValues(Object value1, Object value2) {
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
	public Object cloneValue(Object value);
	
	/**
	 * Uses the hashCode of the name of this <tt>type</tt>.<br>
	 * In essence, {@code hashCode() = getName().hashCode()} {@inheritDoc}
	 */
	@Override
	public int hashCode();
	
}