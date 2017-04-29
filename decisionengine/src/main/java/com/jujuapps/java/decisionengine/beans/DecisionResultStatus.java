package com.jujuapps.java.decisionengine.beans;

public enum DecisionResultStatus {
	DECISION_AVAILABLE,
	DECISION_NOT_AVAILABLE;
	
	public DecisionResultStatus fromString(String val) {
		return valueOf(val);
	}
	
	public String toString(DecisionResultStatus op) {
		return op.toString();
	}
}
