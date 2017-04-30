package com.jujuapps.java.decisionengine.beans;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class DecisionRequest {

	private Map<String, Object> attributes;
	private List<Rule> rules;
	private DecisionStrategy decisionStrategy;
	private List<String> decisionPriorities;
	public List<String> getDecisionPriorities() {
		return decisionPriorities;
	}
	public void setDecisionPriorities(List<String> decisionPriorities) {
		this.decisionPriorities = decisionPriorities;
	}
	public DecisionStrategy getDecisionStrategy() {
		return decisionStrategy;
	}
	public void setDecisionStrategy(DecisionStrategy decisionStrategy) {
		this.decisionStrategy = decisionStrategy;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	} 

}
