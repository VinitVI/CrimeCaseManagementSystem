package com.cms.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "caseFile")
@EqualsAndHashCode(exclude = "caseFile")
public class Note {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    // Optional: backend can set this using logged-in officer
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseFile;
}
