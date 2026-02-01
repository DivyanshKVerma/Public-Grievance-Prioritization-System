package com.pgps.model;

import java.time.LocalDateTime;
import com.pgps.enums.*;

public class Grievance {

    private int grievanceId;
    private String title;
    private String description;
    private GrievanceCategory category;
    private SeverityLevel severity;
    private GrievanceStatus status;
    private int priorityScore;
    private int raisedByUserId;
    private int assignedAuthorityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Grievance() {}

    public Grievance(int grievanceId, String title, String description,
                     GrievanceCategory category, SeverityLevel severity,
                     GrievanceStatus status, int priorityScore,
                     int raisedByUserId, int assignedAuthorityId,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.grievanceId = grievanceId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.severity = severity;
        this.status = status;
        this.priorityScore = priorityScore;
        this.raisedByUserId = raisedByUserId;
        this.assignedAuthorityId = assignedAuthorityId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getGrievanceId() {
        return grievanceId;
    }

    public void setGrievanceId(int grievanceId) {
        this.grievanceId = grievanceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GrievanceCategory getCategory() {
        return category;
    }

    public void setCategory(GrievanceCategory category) {
        this.category = category;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = severity;
    }

    public GrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(GrievanceStatus status) {
        this.status = status;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(int priorityScore) {
        this.priorityScore = priorityScore;
    }

    public int getRaisedByUserId() {
        return raisedByUserId;
    }

    public void setRaisedByUserId(int raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
    }

    public int getAssignedAuthorityId() {
        return assignedAuthorityId;
    }

    public void setAssignedAuthorityId(int assignedAuthorityId) {
        this.assignedAuthorityId = assignedAuthorityId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
