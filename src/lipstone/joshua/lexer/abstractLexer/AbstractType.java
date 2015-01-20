package lipstone.joshua.lexer.abstractLexer;

import java.lang.reflect.InvocationTargetException;

/**
 * A class implementation of {@link GenericType}.<br>
 * This is included for uses of this library that do not use enums for types.
 * 
 * @author Joshua Lipstone
 */
public class AbstractType implements GenericType {
	protected final String name, open, close;
	protected final int hash;
	
	public AbstractType(String name, String open, String close) {
		this.name = name;
		this.open = open;
		this.close = close;
		hash = name.hashCode();
	}
	
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
	public Object cloneValue(Object value) {
		try {
			return value instanceof Cloneable ? value.getClass().getMethod("clone").invoke(value) : value;
		}
		catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			return value;
		}
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
}
