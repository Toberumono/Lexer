package lexer;

public class Type {
	
	private final String name;
	
	/**
	 * @return the name of this Type
	 */
	public final String getName() {
		return name;
	}
	
	public Type(String name) {
		this.name = name;
	}
	
	/**
	 * By default this simply forwards to the value's toString() method. However, this can be overridden for
	 * implementation-specific methods.
	 * 
	 * @param value
	 * @return the value as a <tt>String</tt>
	 */
	public String valueToString(Object value) {
		return value.toString();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o instanceof Type)
			return ((Type) o).name.equals(name);
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
		if (value1 instanceof Comparable)
			return ((Comparable<Object>) value1).compareTo(value2);
		return 0;
	}
}
