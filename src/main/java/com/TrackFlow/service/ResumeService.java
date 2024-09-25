package com.TrackFlow.service;

import com.TrackFlow.dto.ResumeDTO;
import com.TrackFlow.model.Resume;
import com.TrackFlow.model.User;
import com.TrackFlow.repository.ResumeRepository;
import com.TrackFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final String uploadDir = "uploads/resumes/";

    @Autowired
    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public ResumeDTO uploadResume(MultipartFile file, UUID userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + UUID.randomUUID() + "_" + fileName;

        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileName(fileName);
        resume.setFilePath(filePath);

        Resume savedResume = resumeRepository.save(resume);
        return convertToDTO(savedResume);
    }

    public ResumeDTO getResumeById(UUID id) {
        return resumeRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<ResumeDTO> getResumesByUserId(UUID userId) {
        return resumeRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteResume(UUID id) throws IOException {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Files.deleteIfExists(Paths.get(resume.getFilePath()));
        resumeRepository.deleteById(id);
    }

    private ResumeDTO convertToDTO(Resume resume) {
        ResumeDTO dto = new ResumeDTO();
        dto.setId(resume.getId());
        dto.setUserId(resume.getUser().getId());
        dto.setFileName(resume.getFileName());
        dto.setUploadedAt(resume.getUploadedAt());
        return dto;
    }
}