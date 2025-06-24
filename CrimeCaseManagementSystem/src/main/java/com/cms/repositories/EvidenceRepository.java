package com.cms.repositories;

import com.cms.models.Case;
import com.cms.models.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    // 🔍 Correct method name matching the field name in Evidence entity
    List<Evidence> findByEvidenceCase(Case evidenceCase);
}
