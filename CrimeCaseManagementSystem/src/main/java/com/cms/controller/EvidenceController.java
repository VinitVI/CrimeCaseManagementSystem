package com.cms.controller;

import com.cms.models.Case;
import com.cms.models.Evidence;
import com.cms.repositories.CaseRepository;
import com.cms.service.EvidenceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession; // For storing temporary notes

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/officer/case")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    @Autowired
    private CaseRepository caseRepository;

    // Show standalone evidence form (optional use)
    @GetMapping("/add-evidence/{caseId}")
    public String showEvidenceForm(@PathVariable Long caseId, Model model) {
        Case caseFile = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));
        model.addAttribute("caseFile", caseFile);
        model.addAttribute("evidence", new Evidence());
        return "officer/add-evidence";
    }

    // Handle evidence form submission from modal
    @PostMapping("/add-evidence")
    public String submitEvidence(@RequestParam("caseId") Long caseId,
                                 @RequestParam("evidenceTitle") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "notes", required = false) String notes,
                                 @RequestParam("file") MultipartFile file,
                                 HttpSession session) {
        try {
            // Step 1: Fetch Case
            Case caseFile = caseRepository.findById(caseId)
                    .orElseThrow(() -> new RuntimeException("Case not found"));

            // Step 2: Upload File
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath.toFile());

            // Step 3: Save Evidence
            Evidence evidence = new Evidence();
            evidence.setEvidenceTitle(title);
            evidence.setDescription(description);
            evidence.setFilePath("/uploads/" + fileName);
            evidence.setEvidenceCase(caseFile);

            evidenceService.saveEvidence(evidence);

            // Step 4: Handle Notes (store only in session)
            if (notes != null && !notes.trim().isEmpty()) {
                List<String> recentActions = (List<String>) session.getAttribute("recentActions");
                if (recentActions == null) recentActions = new ArrayList<>();
                recentActions.add("📝 Note on Case #" + caseId + ": " + notes.trim());
                session.setAttribute("recentActions", recentActions);
            }

            return "redirect:/officer/dashboard?success";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/officer/view-cases?error";
        }
    }

    // Download evidence file
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadEvidenceFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
