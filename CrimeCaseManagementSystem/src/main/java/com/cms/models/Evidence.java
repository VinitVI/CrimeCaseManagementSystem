package com.cms.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "evidenceCase")
@EqualsAndHashCode(exclude = "evidenceCase")
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evidence_title", nullable = false)
    private String evidenceTitle;

    @Column(nullable = false)
    private String description;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;

    @Column(name = "note")
    private String note; // ✅ NEW FIELD for officer remarks or notes

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case evidenceCase;

    @PrePersist
    protected void onCreate() {
        this.submittedDate = LocalDateTime.now();
    }
}
