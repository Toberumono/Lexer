package toberumono.lexer;

import toberumono.structures.sexps.ConsCellType;

/**
 * An implementation of {@link ConsCellType}.
 * 
 * @author Toberumono
 */
public class Type extends ConsCellType {
	
	/**
	 * A pre-created type that <i>must</i> be used to denote empty values (e.g. the end of a list)
	 */
	public static final Type EMPTY = new Type("Empty") {
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
	 * A pre-created {@link Type} that flags the value as an instance of {@link ConsCell}
	 */
	public static final Type TOKEN = new Type("ConsCell") {
		
		@Override
		public ConsCell cloneValue(Object value) {
			return ((ConsCell) value).clone();
		}
	};
	
	/**
	 * Constructs a {@link Type} with the given name and descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 */
	public Type(String name, String open, String close) {
		super(name, open, close);
	}

	/**
	 * Constructs a {@link Type} with the given name and no descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 */
	public Type(String name) {
		super(name);
	}
}
