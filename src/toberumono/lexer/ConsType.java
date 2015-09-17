package toberumono.lexer;

import toberumono.structures.sexps.ConsCellType;

/**
 * An implementation of {@link ConsCellType}.
 * 
 * @author Toberumono
 */
public class ConsType extends ConsCellType {
	
	/**
	 * A pre-created type that <i>must</i> be used to denote empty values (e.g. the end of a list)
	 */
	public static final ConsType EMPTY = new ConsType("Empty") {
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
	 * A pre-created {@link ConsType} that flags the value as an instance of {@link ConsCell}
	 */
	public static final ConsType TOKEN = new ConsType("ConsCell") {
		
		@Override
		public ConsCell cloneValue(Object value) {
			return ((ConsCell) value).clone();
		}
	};
	
	/**
	 * Constructs a {@link ConsType} with the given name and descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 * @param open
	 *            the open token
	 * @param close
	 *            the close token
	 */
	public ConsType(String name, String open, String close) {
		super(name, open, close);
	}

	/**
	 * Constructs a {@link ConsType} with the given name and no descender tokens.
	 * 
	 * @param name
	 *            the name of the type
	 */
	public ConsType(String name) {
		super(name);
	}
}
