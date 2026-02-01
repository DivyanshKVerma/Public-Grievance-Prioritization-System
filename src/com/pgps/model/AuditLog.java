package com.pgps.model;

import java.time.LocalDateTime;

public class AuditLog {

    private int logId;
    private int grievanceId;
    private String action;
    private String performedBy;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(int logId,
                    int grievanceId,
                    String action,
                    String performedBy,
                    LocalDateTime timestamp) {

        this.logId = logId;
        this.grievanceId = grievanceId;
        this.action = action;
        this.performedBy = performedBy;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public int getGrievanceId() {
        return grievanceId;
    }

    public String getAction() {
        return action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setGrievanceId(int grievanceId) {
        this.grievanceId = grievanceId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}