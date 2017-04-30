package com.jujuapps.java.decisionengine.beans;

public enum DecisionStrategy {
	FirstMatch,
	LastMatch,
	LeastMatch,
	MostMatch,
	PriorityMatch;
	
	public DecisionStrategy fromString(String val) {
		return valueOf(val);
	}
	
	public String toString(DecisionStrategy op) {
		return op.toString();
	}
}
