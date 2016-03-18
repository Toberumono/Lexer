package demos.math.advanced;

import java.util.function.BinaryOperator;

import toberumono.structures.sexpressions.ConsCell;

/**
 * An extension (and wrapper) of a {@link BinaryOperator} that performs a mathematical operation. This stores the priority of
 * the operation for the order of operations requirement of the implementation.
 * 
 * @author Toberumono
 */
public class Operator implements BinaryOperator<ConsCell> {
	private final int priority;
	private final BinaryOperator<ConsCell> operation;
	
	/**
	 * Constructs a new {@link Operator} with the given priority and {@link BinaryOperator operation}.
	 * 
	 * @param priority
	 *            the priority of the {@link Operator} (higher priority gets executed first)
	 * @param operation
	 *            the operation to perform
	 */
	public Operator(int priority, BinaryOperator<ConsCell> operation) {
		this.priority = priority;
		this.operation = operation;
	}
	
	/**
	 * @return the precedence of the {@link Operator}
	 */
	public int getPrecedence() {
		return priority;
	}
	
	@Override
	public ConsCell apply(ConsCell t, ConsCell u) {
		return operation.apply(t, u);
	}
}
