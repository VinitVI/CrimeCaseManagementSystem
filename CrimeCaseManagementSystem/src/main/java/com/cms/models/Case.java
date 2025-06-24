package com.cms.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"assignedOfficer", "complaint", "evidences"})
@EqualsAndHashCode(exclude = {"assignedOfficer", "complaint", "evidences"})
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne
    @JoinColumn(name = "assigned_officer_id", nullable = false)
    private Authority assignedOfficer;

    @Column(name = "police_station", nullable = false)
    private String policeStation;

    @Column(name = "case_section", nullable = false)
    private String section;

    @Column(name = "case_type", nullable = false)
    private String caseType;

    @Column(name = "status", nullable = false)
    private String status = "ASSIGNED";

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate = LocalDate.now();

    // ✅ Add this field to link evidences with this case
    @OneToMany(mappedBy = "evidenceCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evidence> evidences;
}
