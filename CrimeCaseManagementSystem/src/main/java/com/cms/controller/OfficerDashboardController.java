package com.cms.controller;

import com.cms.models.Authority;
import com.cms.models.Case;
import com.cms.models.Evidence;
import com.cms.repositories.AuthorityRepository;
import com.cms.repositories.CaseRepository;
import com.cms.service.EvidenceService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/officer")
public class OfficerDashboardController {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private EvidenceService evidenceService;

    // ✅ Officer dashboard summary + recent session actions
    @GetMapping("/dashboard")
    public String showOfficerDashboard(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Authority officer = authorityRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Officer not found: " + username));

        long totalCases = caseRepository.countByAssignedOfficer(officer);
        long activeCases = caseRepository.countByAssignedOfficerAndStatus(officer, "ACTIVE");
        long solvedCases = caseRepository.countByAssignedOfficerAndStatus(officer, "SOLVED");

        model.addAttribute("totalCases", totalCases);
        model.addAttribute("activeCases", activeCases);
        model.addAttribute("solvedCases", solvedCases);

        // ✅ Show recent actions from session
        List<String> recentActions = (List<String>) session.getAttribute("recentActions");
        model.addAttribute("recentActions", recentActions);

        return "officer/dashboard";
    }

    // ✅ View assigned cases with evidence attached
    @GetMapping("/view-cases")
    public String viewCases(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Authority officer = authorityRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Officer not found: " + username));

        List<Case> cases = caseRepository.findByAssignedOfficer(officer);

        for (Case c : cases) {
            List<Evidence> evidences = evidenceService.getEvidencesByCase(c);
            c.setEvidences(evidences);
        }

        model.addAttribute("cases", cases);
        return "officer/view-cases";
    }

    // ✅ Activate a case and push recent action to session
    @PostMapping("/case/activate")
    public String activateCase(@RequestParam("caseId") Long caseId, HttpSession session) {
        Case caseObj = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));

        if ("ASSIGNED".equalsIgnoreCase(caseObj.getStatus())) {
            caseObj.setStatus("ACTIVE");
            caseRepository.save(caseObj);

            List<String> recentActions = (List<String>) session.getAttribute("recentActions");
            if (recentActions == null) {
                recentActions = new ArrayList<>();
            }

            recentActions.add(0, "⚡ Case_" + caseId + " has been activated.");
            if (recentActions.size() > 5) {
                recentActions = recentActions.subList(0, 5);
            }

            session.setAttribute("recentActions", recentActions);
        }

        return "redirect:/officer/view-cases";
    }

    // ✅ Mark case as solved
    @PostMapping("/case/solve")
    public String solveCase(@RequestParam("caseId") Long caseId, HttpSession session) {
        Case caseObj = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));

        if ("ACTIVE".equalsIgnoreCase(caseObj.getStatus())) {
            caseObj.setStatus("SOLVED");
            caseRepository.save(caseObj);

            List<String> recentActions = (List<String>) session.getAttribute("recentActions");
            if (recentActions == null) {
                recentActions = new ArrayList<>();
            }

            recentActions.add(0, "✅ Case_" + caseId + " has been solved.");
            if (recentActions.size() > 5) {
                recentActions = recentActions.subList(0, 5);
            }

            session.setAttribute("recentActions", recentActions);
        }

        return "redirect:/officer/view-cases";
    }

    // ✅ Add temporary note from evidence form (not saved to DB)
    @PostMapping("/add-note")
    public String addTemporaryNote(@RequestParam("caseId") Long caseId,
                                   @RequestParam("notes") String note,
                                   HttpSession session) {
        if (note != null && !note.trim().isEmpty()) {
            List<String> recentActions = (List<String>) session.getAttribute("recentActions");
            if (recentActions == null) {
                recentActions = new ArrayList<>();
            }

            recentActions.add(0, "📝 Note added to Case #" + caseId + ": " + note.trim());
            if (recentActions.size() > 5) {
                recentActions = recentActions.subList(0, 5);
            }

            session.setAttribute("recentActions", recentActions);
        }

        return "redirect:/officer/view-cases";
    }
}
