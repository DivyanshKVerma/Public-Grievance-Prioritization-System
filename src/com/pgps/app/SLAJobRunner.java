package com.pgps.app;

import com.pgps.service.SLAEscalationService;

public class SLAJobRunner {
    public static void main(String[] args) {
        new SLAEscalationService().checkAndEscalate();
        System.out.println("SLA check completed");
    }
}