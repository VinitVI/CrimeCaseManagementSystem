package com.cms.controller;

import com.cms.models.Authority;
import com.cms.models.Case;
import com.cms.models.Complaint;
import com.cms.models.Role;
import com.cms.repositories.AuthorityRepository;
import com.cms.repositories.CaseRepository;
import com.cms.repositories.ComplaintRepository;
import com.cms.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    // ✅ Admin dashboard with summary + officer case count
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        long totalComplaints = complaintRepository.count();
        long activeCases = caseRepository.countByStatus("ACTIVE");
        long solvedCases = caseRepository.countByStatus("SOLVED");

        model.addAttribute("totalComplaints", totalComplaints);
        model.addAttribute("activeCases", activeCases);
        model.addAttribute("solvedCases", solvedCases);

        // Dynamic officer-case count list
        List<Authority> officers = authorityRepository.findByRole(Role.ROLE_OFFICER);
        Map<Authority, Long> officerCaseCounts = new LinkedHashMap<>();

        for (Authority officer : officers) {
            long count = caseRepository.countByAssignedOfficer(officer);
            officerCaseCounts.put(officer, count);
        }

        model.addAttribute("officerCaseCounts", officerCaseCounts);

        return "admin/dashboard";
    }

    // ✅ Complaint list view
    @GetMapping("/usercomplaints")
    public String showUserComplaints(Model model) {
        List<Complaint> complaints = complaintService.getAllComplaints();
        model.addAttribute("complaints", complaints);

        List<Authority> officers = authorityRepository.findByRole(Role.ROLE_OFFICER);
        model.addAttribute("officers", officers);

        return "admin/complaints";
    }

    // ✅ Handle case creation
    @PostMapping("/create-case")
    public String createCase(
            @RequestParam("complaintId") Long complaintId,
            @RequestParam("officerId") Long officerId,
            @RequestParam("policeStation") String policeStation,
            @RequestParam("section") String section,
            RedirectAttributes redirectAttributes
    ) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Complaint ID"));

        Authority officer = authorityRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Officer ID"));

        Case newCase = new Case();
        newCase.setComplaint(complaint);
        newCase.setAssignedOfficer(officer);
        newCase.setPoliceStation(policeStation);
        newCase.setSection(section);
        newCase.setCaseType(complaint.getComplaintType());
        newCase.setStatus("ASSIGNED");
        newCase.setAssignedDate(LocalDate.now());

        caseRepository.save(newCase);

        complaint.setStatus("CASE_CREATED");
        complaintRepository.save(complaint);

        redirectAttributes.addFlashAttribute("successMessage", "Case created successfully!");
        return "redirect:/admin/dashboard";
    }

    // ✅ View all cases
    @GetMapping("/view-cases")
    public String viewAllCases(Model model) {
        List<Case> allCases = caseRepository.findAll();
        model.addAttribute("cases", allCases);
        return "admin/cases";
    }
}
