package com.jujuapps.java.decisionengine.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class Rule {

	private List<String> leftTerms;
	private List<Object> rightTerms;
	private Operator operator;
	private String decision;
	public List<String> getLeftTerms() {
		return leftTerms;
	}
	public void setLeftTerms(List<String> leftTerms) {
		this.leftTerms = leftTerms;
	}
	public List<Object> getRightTerms() {
		return rightTerms;
	}
	public void setRightTerms(List<Object> rightTerms) {
		this.rightTerms = rightTerms;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	

}
