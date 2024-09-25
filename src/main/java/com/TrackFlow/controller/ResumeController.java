package com.TrackFlow.controller;

import com.TrackFlow.dto.ResumeDTO;
import com.TrackFlow.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeDTO> uploadResume(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("userId") UUID userId) throws IOException {
        ResumeDTO resumeDTO = resumeService.uploadResume(file, userId);
        return ResponseEntity.ok(resumeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResume(@PathVariable UUID id) throws IOException {
        ResumeDTO resumeDTO = resumeService.getResumeById(id);
        if (resumeDTO == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(resumeDTO.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeDTO.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResumeDTO>> getResumesByUserId(@PathVariable UUID userId) {
        List<ResumeDTO> resumes = resumeService.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable UUID id) throws IOException {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}