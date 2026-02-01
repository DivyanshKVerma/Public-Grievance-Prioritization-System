package com.pgps.dao;

import java.util.List;

import com.pgps.enums.GrievanceStatus;
import com.pgps.model.Grievance;



public interface GrievanceDAO {
    void updateEscalation(int grievanceId, GrievanceStatus status, int newAuthorityId);
    void save(Grievance grievance);
    Grievance findById(int grievanceId);
    List<Grievance> findAll();
    void updateStatus(int grievanceId, String status);
    List<Grievance> findByAuthorityId(int authorityId);
    List<Grievance> findByAuthority(int authorityId);
    

    
}



