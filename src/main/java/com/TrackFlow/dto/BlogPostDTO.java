package com.TrackFlow.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BlogPostDTO {
    private UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}