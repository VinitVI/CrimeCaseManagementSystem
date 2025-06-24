package com.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.models.Note;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
}