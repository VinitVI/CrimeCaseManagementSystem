//package com.cms.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//        System.out.println("Login successful for user: " + authentication.getName());
//        System.out.println("User roles: " + authentication.getAuthorities());
//
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            String role = authority.getAuthority();
//
//            if ("ROLE_ADMIN".equals(role)) {
//                System.out.println("Redirecting to /admin/dashboard");
//                response.sendRedirect("/admin/dashboard");
//                return;
//            } else if ("ROLE_OFFICER".equals(role)) {
//                System.out.println("Redirecting to /officer/dashboard");
//                response.sendRedirect("/officer/dashboard");
//                return;
//            }
//        }
//
//        // Default fallback
//        System.out.println("Redirecting to /home (default)");
//        response.sendRedirect("/home");
//    }
//}
