package com.pgps.service;

import com.pgps.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class SlaPolicy {

    public static final Map<SeverityLevel, Integer> SLA_HOURS = Map.of(
            SeverityLevel.LOW, 72,
            SeverityLevel.MEDIUM, 48,
            SeverityLevel.HIGH, 24,
            SeverityLevel.CRITICAL, 6
    );

    public static int calculateTimeWeight(
            SeverityLevel severity,
            LocalDateTime createdAt) {

        long hoursPassed =
                Duration.between(createdAt, LocalDateTime.now()).toHours();

        int sla = SLA_HOURS.get(severity);

        if (hoursPassed >= sla) {
            return 20; // SLA breached
        } else if (hoursPassed >= sla / 2) {
            return 10; // nearing breach
        }
        return 0;
    }
}
