package toberumono.lexer.genericBase;

/**
 * A class implementation of {@link GenericType}.<br>
 * This is included for uses of this library that do not use enums for types.
 * 
 * @author Toberumono
 */
public class AbstractType implements GenericType {
	protected final String name, open, close;
	protected final int hash;
	
	/**
	 * Constructs an {@link AbstractType} with the given name and descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 */
	public AbstractType(String name, String open, String close) {
		this.name = name;
		this.open = open;
		this.close = close;
		hash = name.hashCode();
	}
	
	/**
	 * Constructs an {@link AbstractType} with the given name and no descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 */
	public AbstractType(String name) {
		this(name, null, null);
	}
	
	@Override
	public final String getOpen() {
		return open;
	}
	
	@Override
	public final String getClose() {
		return close;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final boolean marksDescender() {
		return open != null;
	}
	
	@Override
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
	
	@Override
	public int hashCode() {
		return hash;
	}
}
