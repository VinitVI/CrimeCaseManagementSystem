//package com.cms.config;
//
//import com.cms.service.AuthorityDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final AuthorityDetailsService authorityDetailsService;
//    private final AuthenticationSuccessHandler successHandler;
//
//    public SecurityConfig(AuthorityDetailsService authorityDetailsService, AuthenticationSuccessHandler successHandler) {
//        this.authorityDetailsService = authorityDetailsService;
//        this.successHandler = successHandler;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                    "/", "/home", "/authority/signup", "/authority/login",
//                    "/user/complaint/add", "/user/complaint/submit",
//                    "/css/**", "/js/**", "/images/**", "/Images/**"
//                ).permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/officer/**").hasRole("OFFICER")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/authority/login")
//                .loginProcessingUrl("/do-login") // ✅ fixed
//                .successHandler(successHandler)
//                .failureHandler(failureHandler()) // ✅ optional
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/authority/login?logout")
//                .permitAll()
//            );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler failureHandler() {
//        return (request, response, exception) -> {
//            System.out.println("❌ Login failed: " + exception.getMessage());
//            response.sendRedirect("/authority/login?error=true");
//        };
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return authorityDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // ✅ secure version
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}

package com.cms.config;

import com.cms.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                             PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/", "/home","/user/complaint/add", "/user/complaint/submit","/signup", "/login","/images/**", "/Images/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/officer/**").hasRole("OFFICER")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/redirect", true)
                    .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/home") // 👈 Redirect to home
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll();
        return http.build();
    }
}

