package com.pgps.service;

import com.pgps.model.Grievance;

public class PriorityScoringService {

    public int calculatePriority(Grievance g) {

        int severityScore =
                PriorityPolicy.SEVERITY_WEIGHT.get(g.getSeverity());

        int categoryScore =
                PriorityPolicy.CATEGORY_WEIGHT.get(g.getCategory());

        int timeScore =
                SlaPolicy.calculateTimeWeight(
                        g.getSeverity(),
                        g.getCreatedAt()
                );

        return severityScore + categoryScore + timeScore;
    }
}