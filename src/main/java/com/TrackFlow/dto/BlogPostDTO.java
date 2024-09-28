package com.TrackFlow.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private String imagePath;
    private MultipartFile imageFile;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}