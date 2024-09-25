package com.TrackFlow.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResumeDTO {
    private UUID id;
    private UUID userId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;
}