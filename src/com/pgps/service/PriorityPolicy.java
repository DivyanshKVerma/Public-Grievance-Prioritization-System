package com.pgps.service;

import com.pgps.enums.*;

import java.util.Map;

public class PriorityPolicy {

    public static final Map<SeverityLevel, Integer> SEVERITY_WEIGHT = Map.of(
            SeverityLevel.LOW, 10,
            SeverityLevel.MEDIUM, 25,
            SeverityLevel.HIGH, 40,
            SeverityLevel.CRITICAL, 50
    );

    public static final Map<GrievanceCategory, Integer> CATEGORY_WEIGHT = Map.of(
            GrievanceCategory.INFRASTRUCTURE, 10,
            GrievanceCategory.ELECTRICITY, 15,
            GrievanceCategory.WATER, 20,
            GrievanceCategory.LAW_ORDER, 25,
            GrievanceCategory.HEALTH, 30
    );
}
