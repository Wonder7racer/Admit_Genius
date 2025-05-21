package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String fileType;
    
    private Long fileSize;
    
    private String filePath;
    
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private DocumentStatus status = DocumentStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
   
    
    public enum DocumentStatus {
        PENDING, APPROVED, REJECTED
    }
    
    public enum DocumentType {
        TRANSCRIPT, RECOMMENDATION, CERTIFICATE, RESUME, FINANCIAL, OTHER
    }
} 