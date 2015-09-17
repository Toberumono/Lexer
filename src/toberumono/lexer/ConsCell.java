package toberumono.lexer;

import toberumono.structures.sexps.GenericConsCell;

/**
 * An implementation of {@link GenericConsCell}
 * 
 * @author Toberumono
 */
public class ConsCell extends GenericConsCell<Type, ConsCell> {
	
	/**
	 * Constructs a {@link ConsCell} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 */
	public ConsCell(Object car, Type carType, Object cdr, Type cdrType) {
		super(car, carType, cdr, cdrType, ConsCell::new, Type.TOKEN, Type.EMPTY);
	}
	
	/**
	 * Constructs a {@link ConsCell} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 */
	public ConsCell(Object car, Type carType) {
		super(car, carType, ConsCell::new, Type.TOKEN, Type.EMPTY);
	}
	
	/**
	 * Constructs an empty {@link ConsCell}.
	 */
	public ConsCell() {
		super(ConsCell::new, Type.TOKEN, Type.EMPTY);
	}
}
