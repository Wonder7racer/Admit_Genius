package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "essay_requirements")
public class EssayRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school; // Can be null if program-specific

    @ManyToOne
    @JoinColumn(name = "program_id")
    private SchoolProgram program; // Can be null if school-wide

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    private Integer wordLimit;

    private boolean isRequired = true;

    @Column(columnDefinition = "TEXT")
    private String notes;
} 