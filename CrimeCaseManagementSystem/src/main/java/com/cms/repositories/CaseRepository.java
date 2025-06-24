package com.cms.repositories;

import com.cms.models.Case;
import com.cms.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    // 🔹 Get all cases assigned to a specific officer
    List<Case> findByAssignedOfficer(Authority officer);

    // 🔹 Get cases by officer and status (e.g., ACTIVE or SOLVED)
    List<Case> findByAssignedOfficerAndStatus(Authority officer, String status);

    // 🔹 Count total cases assigned to officer
    long countByAssignedOfficer(Authority officer);

    // 🔹 Count officer's cases by status (for dashboard summary)
    long countByAssignedOfficerAndStatus(Authority officer, String status);

    // 🔹 Count all cases by global status (used in admin dashboard)
    long countByStatus(String status);

    // 🔹 Get all cases by status (for admin solved cases)
    List<Case> findByStatus(String status);
}
