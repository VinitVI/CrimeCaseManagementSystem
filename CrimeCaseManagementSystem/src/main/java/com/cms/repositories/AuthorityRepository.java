package com.cms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.models.Authority;
import com.cms.models.Role;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByUsernameIgnoreCase(String username);

    // Now accepts the Role enum
    List<Authority> findByRole(Role role);
}

