package com.pgps.service;

import com.pgps.model.Grievance;

public class FinalPriorityEngine {

    private final PriorityScoringService ruleEngine =
            new PriorityScoringService();

    private final AIPriorityAdvisor aiAdvisor =
            new SimpleNlpPriorityAdvisor();

    public int calculateFinalPriority(Grievance g) {

        int ruleScore = ruleEngine.calculatePriority(g);
        int aiBoost = aiAdvisor.suggestUrgencyBoost(g);

        return ruleScore + aiBoost;
    }

    public String explain(Grievance g) {
        return aiAdvisor.explainDecision(g);
    }
}
