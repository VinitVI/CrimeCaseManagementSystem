package com.cms.service;

import com.cms.models.Complaint;
import com.cms.models.User;
import com.cms.repositories.ComplaintRepository; // ✅ Import this
import com.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository; // ✅ Injected here

    @Transactional
    public Complaint saveComplaint(Complaint complaint, User user) {
        if (user.getComplaints() == null) {
            user.setComplaints(new ArrayList<>());
        }

        user.getComplaints().add(complaint);
        complaint.setUser(user);

        if (complaint.getStatus() == null) {
            complaint.setStatus("pending");
        }

        userRepository.save(user);
        return complaint;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll(); // ✅ This will now work
    }
}
