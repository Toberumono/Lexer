package lexer;

public abstract class Type {
	
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
	
	public abstract int compareValues(Object value1, Object value2);
}
