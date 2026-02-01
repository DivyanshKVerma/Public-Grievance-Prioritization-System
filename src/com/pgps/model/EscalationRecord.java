package com.pgps.model;

import java.time.LocalDateTime;

public class EscalationRecord {

    private int escalationId;
    private int grievanceId;
    private int fromAuthorityId;
    private int toAuthorityId;
    private String reason;
    private LocalDateTime escalatedAt;

    public EscalationRecord() {}

    public EscalationRecord(int escalationId,
                            int grievanceId,
                            int fromAuthorityId,
                            int toAuthorityId,
                            String reason,
                            LocalDateTime escalatedAt) {

        this.escalationId = escalationId;
        this.grievanceId = grievanceId;
        this.fromAuthorityId = fromAuthorityId;
        this.toAuthorityId = toAuthorityId;
        this.reason = reason;
        this.escalatedAt = escalatedAt;
    }

    public int getEscalationId() {
        return escalationId;
    }

    public int getGrievanceId() {
        return grievanceId;
    }

    public int getFromAuthorityId() {
        return fromAuthorityId;
    }

    public int getToAuthorityId() {
        return toAuthorityId;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalationId(int escalationId) {
        this.escalationId = escalationId;
    }

    public void setGrievanceId(int grievanceId) {
        this.grievanceId = grievanceId;
    }

    public void setFromAuthorityId(int fromAuthorityId) {
        this.fromAuthorityId = fromAuthorityId;
    }

    public void setToAuthorityId(int toAuthorityId) {
        this.toAuthorityId = toAuthorityId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setEscalatedAt(LocalDateTime escalatedAt) {
        this.escalatedAt = escalatedAt;
    }
}