package demos.math.advanced;

import java.util.function.BinaryOperator;

import toberumono.structures.sexpressions.ConsCell;

public class Operator implements BinaryOperator<ConsCell> {
	private final int priority;
	private final BinaryOperator<ConsCell> operation;
	
	public Operator(int priority, BinaryOperator<ConsCell> operation) {
		this.priority = priority;
		this.operation = operation;
	}
	
	public int getPrecendence() {
		return priority;
	}
	
	@Override
	public ConsCell apply(ConsCell t, ConsCell u) {
		return operation.apply(t, u);
	}
}
