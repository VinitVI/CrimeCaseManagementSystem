package com.cms.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complaint_type", nullable = false)
    private String complaintType;

    @Column(name = "title", nullable = false)
    private String title;

    // Single column for date and time of incident
    @Column(name = "date_time_of_incident", nullable = false)
    private LocalDateTime dateTimeOfIncident;

    @Column(name = "location_of_incident", nullable = false)
    private String locationOfIncident;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
