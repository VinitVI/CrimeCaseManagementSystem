package com.cms.controller;

import com.cms.dto.ComplaintFormDTO;
import com.cms.models.Complaint;
import com.cms.models.User;
import com.cms.service.ComplaintService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/add")
    public String showAddComplaintForm(Model model) {
        model.addAttribute("complaintFormDTO", new ComplaintFormDTO());
        return "public/complaint_form"; // Thymeleaf form page
    }

    @PostMapping("/submit")
    public String submitComplaint(@ModelAttribute ComplaintFormDTO complaintFormDTO,
                                  RedirectAttributes redirectAttributes) {

        // ✅ Log incoming DTO data
        System.out.println("===> Form received: " + complaintFormDTO);

        // Map DTO to User entity
        User user = new User();
        user.setFullName(complaintFormDTO.getFullName());
        user.setContactNumber(complaintFormDTO.getContactNumber());
        user.setEmail(complaintFormDTO.getEmail());
        user.setAddress(complaintFormDTO.getAddress());

        // Map DTO to Complaint entity
        Complaint complaint = new Complaint();
        complaint.setComplaintType(complaintFormDTO.getComplaintType());
        complaint.setTitle(complaintFormDTO.getTitle());
        complaint.setDateTimeOfIncident(complaintFormDTO.getDateTimeOfIncident());
        complaint.setLocationOfIncident(complaintFormDTO.getLocationOfIncident());
        complaint.setDescription(complaintFormDTO.getDescription());

        // Save both user and complaint
        Complaint savedComplaint = complaintService.saveComplaint(complaint, user);

        // ✅ Log saved complaint
        System.out.println("===> Complaint saved: " + savedComplaint);

        redirectAttributes.addFlashAttribute("successMessage", "Complaint submitted successfully!");

        return "redirect:/home";
    }
    
}
