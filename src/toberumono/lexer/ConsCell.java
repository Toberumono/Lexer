package toberumono.lexer;

import toberumono.structures.sexps.GenericConsCell;

/**
 * An implementation of {@link GenericConsCell}
 * 
 * @author Toberumono
 */
public class ConsCell extends GenericConsCell<ConsType, ConsCell> {
	
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
	public ConsCell(Object car, ConsType carType, Object cdr, ConsType cdrType) {
		super(car, carType, cdr, cdrType, ConsCell::new, ConsType.TOKEN, ConsType.EMPTY);
	}
	
	/**
	 * Constructs a {@link ConsCell} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 */
	public ConsCell(Object car, ConsType carType) {
		super(car, carType, ConsCell::new, ConsType.TOKEN, ConsType.EMPTY);
	}
	
	/**
	 * Constructs an empty {@link ConsCell}.
	 */
	public ConsCell() {
		super(ConsCell::new, ConsType.TOKEN, ConsType.EMPTY);
	}
}
