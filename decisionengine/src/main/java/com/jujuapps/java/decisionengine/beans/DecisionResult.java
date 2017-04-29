package com.jujuapps.java.decisionengine.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class DecisionResult {

	private DecisionResultStatus status = DecisionResultStatus.DECISION_NOT_AVAILABLE;
	private String decision;
	private List<Rule> matchedRules;
	
	public DecisionResultStatus getStatus() {
		return status;
	}
	public void setStatus(DecisionResultStatus status) {
		this.status = status;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public List<Rule> getMatchedRules() {
		return matchedRules;
	}
	public void setMatchedRules(List<Rule> matchedRules) {
		this.matchedRules = matchedRules;
	}
	
}
