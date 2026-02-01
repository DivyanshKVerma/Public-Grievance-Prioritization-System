package com.pgps.service;

import com.pgps.model.Grievance;

public interface AIPriorityAdvisor {

    int suggestUrgencyBoost(Grievance grievance);

    String explainDecision(Grievance grievance);
}
