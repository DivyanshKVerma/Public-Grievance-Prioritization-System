package com.pgps.dao;

import java.util.List;

import com.pgps.model.EscalationRecord;

public interface EscalationDAO {
    void save(EscalationRecord record);
    List<EscalationRecord> findByGrievanceId(int grievanceId);
}