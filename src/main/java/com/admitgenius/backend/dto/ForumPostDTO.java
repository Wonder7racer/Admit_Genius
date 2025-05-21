package com.admitgenius.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumPostDTO {
    private Long id;
    private Long authorId;
    private String authorName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer likes;
    private Integer commentCount;
    private List<CommentDTO> comments;
    private boolean likedByCurrentUser;
} 