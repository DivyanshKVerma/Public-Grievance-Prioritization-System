package com.pgps.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pgps.dao.EscalationDAO;
import com.pgps.dao.EscalationDAOImpl;
import com.pgps.dao.GrievanceDAO;
import com.pgps.dao.GrievanceDAOImpl;
import com.pgps.enums.GrievanceStatus;
import com.pgps.model.EscalationRecord;
import com.pgps.model.Grievance;

public class SLAEscalationService {

    private static final int SLA_HOURS = 24; // simple SLA rule

    private final GrievanceDAO grievanceDao = new GrievanceDAOImpl();
    private final EscalationDAO escalationDao = new EscalationDAOImpl();

    public void checkAndEscalate() {

        List<Grievance> grievances = grievanceDao.findAll();

        for (Grievance g : grievances) {

            if (g.getStatus() != GrievanceStatus.SUBMITTED) {
                continue;
            }

            if (g.getCreatedAt() == null) {
                continue;
            }

            long hours =
                    java.time.Duration.between(
                            g.getCreatedAt(),
                            LocalDateTime.now()
                    ).toHours();

            if (hours >= SLA_HOURS) {

                int oldAuthority = g.getAssignedAuthorityId();
                int newAuthority = oldAuthority + 1;

                grievanceDao.updateEscalation(
                        g.getGrievanceId(),
                        GrievanceStatus.ESCALATED,
                        newAuthority
                );

                escalationDao.save(new EscalationRecord(
                        0,
                        g.getGrievanceId(),
                        oldAuthority,
                        newAuthority,
                        "Automatic SLA escalation",
                        LocalDateTime.now()
                ));
            }
        }
    }
}