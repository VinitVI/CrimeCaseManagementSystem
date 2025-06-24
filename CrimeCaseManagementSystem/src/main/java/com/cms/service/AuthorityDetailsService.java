//package com.cms.service;
//
//import java.util.Collections;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.cms.models.Authority;
//import com.cms.repositories.AuthorityRepository;
//
//@Service
//public class AuthorityDetailsService implements UserDetailsService {
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    	Authority authority = authorityRepository.findByUsernameIgnoreCase(username)
//    	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//
//        return new org.springframework.security.core.userdetails.User(
//                authority.getUsername(),
//                authority.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(authority.getRole().name()))
//        );
//    }
//}
