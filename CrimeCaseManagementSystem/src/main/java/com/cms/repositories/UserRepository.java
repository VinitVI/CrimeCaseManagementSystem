package com.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional: Add custom query methods if needed
}