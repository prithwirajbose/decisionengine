package com.jujuapps.java.decisionengine.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;

import com.jujuapps.java.decisionengine.beans.DecisionRequest;
import com.jujuapps.java.decisionengine.beans.DecisionResult;
import com.jujuapps.java.decisionengine.beans.DecisionResultStatus;
import com.jujuapps.java.decisionengine.beans.DecisionStrategy;
import com.jujuapps.java.decisionengine.beans.Operator;
import com.jujuapps.java.decisionengine.beans.Rule;
import com.jujuapps.java.decisionengine.framework.ApplicationController;
import com.jujuapps.java.decisionengine.framework.Service;
import com.jujuapps.java.decisionengine.framework.ServiceValidationException;

public class DecisionEngineController implements ApplicationController {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Service(path = "/decision")
	public DecisionResult getDecision(DecisionRequest request) throws Exception {
		DecisionResult result = new DecisionResult();
		List<Rule> matchedRules = new ArrayList<Rule>();
		HashMap<String, String> decisionMap = new HashMap<String, String>();

		if (request != null && request.getRules() != null && request.getRules().size() > 0) {
			for (Rule rule : request.getRules()) {
				Operator op = rule.getOperator();
				List<String> leftTerms = rule.getLeftTerms();
				List<Object> rightTerms = rule.getRightTerms();
				if (op == Operator.Equals && leftTerms != null && rightTerms != null) {
					Object argumentVal = request.getAttributes().get(leftTerms.get(0));
					Object ruleVal = rightTerms.get(0);
					if (ConvertUtils.convert(argumentVal, ruleVal.getClass()).equals(ruleVal)) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				} else if (op == Operator.NotEquals && leftTerms != null && rightTerms != null) {
					Object argumentVal = request.getAttributes().get(leftTerms.get(0));
					Object ruleVal = rightTerms.get(0);
					if (!ConvertUtils.convert(argumentVal, ruleVal.getClass()).equals(ruleVal)) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				} else if (op == Operator.GreaterThan && leftTerms != null && rightTerms != null) {
					Comparable argumentVal = (Comparable) request.getAttributes().get(leftTerms.get(0));
					Comparable ruleVal = (Comparable) rightTerms.get(0);
					if (((Comparable) ConvertUtils.convert(argumentVal, ruleVal.getClass())).compareTo(ruleVal) > 0) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				} else if (op == Operator.LesserThan && leftTerms != null && rightTerms != null) {
					Comparable argumentVal = (Comparable) request.getAttributes().get(leftTerms.get(0));
					Comparable ruleVal = (Comparable) rightTerms.get(0);
					if (((Comparable) ConvertUtils.convert(argumentVal, ruleVal.getClass())).compareTo(ruleVal) < 0) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				} else if (op == Operator.LesserThanOrEquals && leftTerms != null && rightTerms != null) {
					Comparable argumentVal = (Comparable) request.getAttributes().get(leftTerms.get(0));
					Comparable ruleVal = (Comparable) rightTerms.get(0);
					if (((Comparable) ConvertUtils.convert(argumentVal, ruleVal.getClass())).compareTo(ruleVal) <= 0) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				} else if (op == Operator.GreaterThanOrEquals && leftTerms != null && rightTerms != null) {
					Comparable argumentVal = (Comparable) request.getAttributes().get(leftTerms.get(0));
					Comparable ruleVal = (Comparable) rightTerms.get(0);
					if (((Comparable) ConvertUtils.convert(argumentVal, ruleVal.getClass())).compareTo(ruleVal) >= 0) {
						addDecisionToMap(rule.getDecision(), decisionMap, request);
						matchedRules.add(rule);
						result.setStatus(DecisionResultStatus.DECISION_AVAILABLE);
					}
				}
			}
		}
		if (matchedRules.size() > 0) {
			result.setMatchedRules(matchedRules);
		}
		setDecisionOutcome(decisionMap, request, result);
		return result;
	}

	private void setDecisionOutcome(HashMap<String, String> decisionMap, DecisionRequest request,
			DecisionResult result) {
		String decision = null;
		DecisionStrategy strategy = request.getDecisionStrategy() != null ? request.getDecisionStrategy()
				: DecisionStrategy.FirstMatch;
		if (strategy == DecisionStrategy.FirstMatch) {
			decision = decisionMap.get("FIRST_ENTRY_ak783642nksjafsfHSiISS@$jjssjdfwyntr2t83");
		} else if (strategy == DecisionStrategy.LastMatch) {
			decision = decisionMap.get("LAST_ENTRY_hhIS&HNkk882h8BS9n2oH19jsjkks&&54n219(hhs66GSPX");
		} else if (strategy == DecisionStrategy.PriorityMatch) {
			for (String decisionPriority : request.getDecisionPriorities()) {
				if (decisionPriority != null && decisionMap.containsKey("COUNT." + decisionPriority)
						&& Integer.parseInt(decisionMap.get("COUNT." + decisionPriority)) > 0) {
					decision = decisionPriority;
					break;
				}
			}
		} else {
			for (String key : decisionMap.keySet()) {
				if (key != null && key.startsWith("COUNT.")) {
					if (decisionMap.containsKey("COUNT." + decision)) {
						int countOfPrevious = Integer.parseInt(decisionMap.get("COUNT." + decision));
						int countOfThis = Integer.parseInt(decisionMap.get(key));
						if (strategy == DecisionStrategy.MostMatch && countOfThis > countOfPrevious) {
							decision = key.substring(6);
						} else if (strategy == DecisionStrategy.LeastMatch && countOfThis < countOfPrevious) {
							decision = key.substring(6);
						}
					} else {
						decision = key.substring(6);
					}
				}
			}
		}
		result.setDecision(decision);
	}

	@SuppressWarnings("unused")
	private void addDecisionToMap(String decision, HashMap<String, String> decisionMap, DecisionRequest request)
			throws Exception {
		if (request.getDecisionStrategy() == DecisionStrategy.PriorityMatch
				&& (request.getDecisionPriorities() == null || !request.getDecisionPriorities().contains(decision))) {
			throw new ServiceValidationException(
					"Decision " + decision + " missing in decisionPriorities for decisionStrategy=PriorityMatch");
		}
		if (!decisionMap.containsKey("FIRST_ENTRY_ak783642nksjafsfHSiISS@$jjssjdfwyntr2t83")) {
			decisionMap.put("FIRST_ENTRY_ak783642nksjafsfHSiISS@$jjssjdfwyntr2t83", decision);
		}
		decisionMap.put("LAST_ENTRY_hhIS&HNkk882h8BS9n2oH19jsjkks&&54n219(hhs66GSPX", decision);
		if (decisionMap.containsKey("COUNT." + decision))
			decisionMap.put("COUNT." + decision,
					String.valueOf(Integer.parseInt(decisionMap.get("COUNT." + decision)) + 1));
		else
			decisionMap.put("COUNT." + decision, "1");
	}

}
