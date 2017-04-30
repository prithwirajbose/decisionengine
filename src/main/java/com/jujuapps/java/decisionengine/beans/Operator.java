package com.jujuapps.java.decisionengine.beans;

public enum Operator {
	Equals,
	LesserThanOrEquals,
	LesserThan,
	GreaterThanOrEquals,
	GreaterThan,
	Or,
	And,
	Not,
	Contains,
	Between,
	StartsWith,
	EndsWith,
	NotEquals;
	
	public Operator fromString(String val) {
		return valueOf(val);
	}
	
	public String toString(Operator op) {
		return op.toString();
	}
}
