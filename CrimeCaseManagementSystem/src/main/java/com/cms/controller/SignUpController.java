//package com.cms.controller;
//
//import com.cms.models.Authority;
//import com.cms.models.Role;
//import com.cms.repositories.AuthorityRepository;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.regex.Pattern;
//
//@Controller
//@RequestMapping("/authority")
//public class SignUpController {
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @GetMapping("/signup")
//    public String showSignupForm(Model model) {
//        model.addAttribute("authority", new Authority());
//        model.addAttribute("roles", Role.values());
//        return "public/signup";  // Thymeleaf template path
//    }
//
//    @PostMapping("/signup")
//    public String processSignup(
//            @Valid @ModelAttribute("authority") Authority authority,
//            BindingResult result,
//            Model model) {
//
//        String username = authority.getUsername().toLowerCase();
//        String password = authority.getPassword();
//
//        // Password pattern: username@gov + 4 digits (case insensitive)
//        Pattern pattern = Pattern.compile("^" + Pattern.quote(username) + "@gov\\d{4}$", Pattern.CASE_INSENSITIVE);
//        if (!pattern.matcher(password).matches()) {
//            result.rejectValue("password", "error.password",
//                    "Password must be in the format: username@gov followed by 4 digits (e.g., admin@gov1234)");
//        }
//
//        // Check if username already exists
//        if (authorityRepository.findByUsernameIgnoreCase(username).isPresent()) {
//
//            result.rejectValue("username", "error.username", "Username already exists");
//        }
//
//        if (result.hasErrors()) {
//            model.addAttribute("roles", Role.values());
//            return "public/signup";
//        }
//
//        // Encode password and save user
//        authority.setUsername(username);
//        authority.setPassword(password);
//        authorityRepository.save(authority);
//
//        return "redirect:/authority/login?signupSuccess";
//    }
//}

package com.cms.controller;

import com.cms.models.Authority;
import com.cms.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class SignUpController {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("authority", new Authority());
        return "public/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("authority") Authority authority,
                                BindingResult result,
                                Model model) {

        String username = authority.getUsername();
        String password = authority.getPassword();

        // Password pattern: username@gov + 4 digits (case insensitive)
        Pattern pattern = Pattern.compile("^" + Pattern.quote(username) + "@gov\\d{4}$", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(password).matches()) {
            result.rejectValue("password", "error.password",
                    "Password must be in the format: username@gov followed by 4 digits (e.g., vinit@gov1234)");
        }

        // If validation error, reload signup page with errors
        if (result.hasErrors()) {
            return "public/signup";
        }

        // Save after encoding
        authority.setPassword(passwordEncoder.encode(password));
        authorityRepository.save(authority);

        return "redirect:/login";
    }
}
