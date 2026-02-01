package com.pgps.service;

import com.pgps.model.Grievance;

public class SimpleNlpPriorityAdvisor implements AIPriorityAdvisor {

    @Override
    public int suggestUrgencyBoost(Grievance g) {

        String text =
                (g.getTitle() + " " + g.getDescription()).toLowerCase();

        int boost = 0;

        if (text.contains("hospital") ||
            text.contains("emergency") ||
            text.contains("child") ||
            text.contains("injured")) {
            boost += 10;
        }

        if (text.contains("no water") ||
            text.contains("no electricity") ||
            text.contains("for days") ||
            text.contains("since")) {
            boost += 5;
        }

        if (text.contains("police") ||
            text.contains("threat") ||
            text.contains("unsafe")) {
            boost += 10;
        }

        return boost;
    }

    @Override
    public String explainDecision(Grievance g) {

        String text =
                (g.getTitle() + " " + g.getDescription()).toLowerCase();

        if (text.contains("hospital") || text.contains("child")) {
            return "AI detected potential health risk keywords.";
        }

        if (text.contains("police") || text.contains("unsafe")) {
            return "AI detected public safety related keywords.";
        }

        if (text.contains("for days") || text.contains("since")) {
            return "AI detected prolonged issue duration.";
        }

        return "No critical urgency signals detected by AI.";
    }
}
