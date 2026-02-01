package com.pgps.service;

import com.pgps.enums.GrievanceStatus;
import com.pgps.model.Grievance;

import java.time.Duration;
import java.time.LocalDateTime;

public class EscalationService {

    public boolean shouldEscalate(Grievance g) {

        if (g.getStatus() == GrievanceStatus.ESCALATED ||
            g.getStatus() == GrievanceStatus.RESOLVED) {
            return false;
        }

        long hoursPassed = Duration.between(g.getCreatedAt(), LocalDateTime.now()).toHours();

        int slaHours =
                SlaPolicy.SLA_HOURS.get(g.getSeverity());

        return hoursPassed >= slaHours;
    }

    public void escalate(Grievance g) {

        g.setStatus(GrievanceStatus.ESCALATED);

        // simple authority escalation (demo logic)
        g.setAssignedAuthorityId(
                g.getAssignedAuthorityId() + 100
        );
    }
}
