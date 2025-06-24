package com.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cms.models.Complaint;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showLandingPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome() {
        return "public/landing";  // Assuming your template is at templates/public/landing.html
    }
    
    
}
