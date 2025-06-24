package com.cms.service;

import com.cms.models.Case;
import com.cms.models.Evidence;
import com.cms.repositories.EvidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenceService {

    @Autowired
    private EvidenceRepository evidenceRepository;

    // 🔍 Fetch all evidences linked to a specific case
    public List<Evidence> getEvidencesByCase(Case caseFile) {
        return evidenceRepository.findByEvidenceCase(caseFile);
    }

    // 💾 Save new evidence (submittedDate handled by @PrePersist)
    public void saveEvidence(Evidence evidence) {
        evidenceRepository.save(evidence);
    }
}
