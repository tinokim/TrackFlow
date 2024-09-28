package com.TrackFlow.service;

import com.TrackFlow.dto.BlogPostDTO;
import com.TrackFlow.model.BlogPost;
import com.TrackFlow.model.User;
import com.TrackFlow.repository.BlogPostRepository;
import com.TrackFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public BlogPostDTO getBlogPostById(UUID id) {
        return blogPostRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public BlogPostDTO updateBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost existingBlogPost = blogPostRepository.findById(blogPostDTO.getId())
            .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        existingBlogPost.setTitle(blogPostDTO.getTitle());
        existingBlogPost.setContent(blogPostDTO.getContent());
        existingBlogPost.setUpdatedAt(LocalDateTime.now());
        
        BlogPost updatedBlogPost = blogPostRepository.save(existingBlogPost);
        return convertToDTO(updatedBlogPost);
    }

    public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO, String username) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        blogPost.setAuthorName(username);
        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());

        // 이미지 처리
        String imagePath = saveImage(blogPostDTO.getImageFile());
        blogPost.setImagePath(imagePath);

        User author = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        blogPost.setAuthor(author);

        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return convertToDTO(savedBlogPost);
    }

    private String saveImage(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                Path uploadPath = Paths.get("src", "main", "resources", "static", "images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                return "/images/" + fileName;
            } catch (IOException e) {
                return "/images/sample.jpg";
            }
        }
        return "/images/sample.jpg";
    }

    private BlogPostDTO convertToDTO(BlogPost blogPost) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        dto.setAuthorName(blogPost.getAuthor().getUsername());
        dto.setCreatedAt(blogPost.getCreatedAt());
        dto.setUpdatedAt(blogPost.getUpdatedAt());
        dto.setImagePath(blogPost.getImagePath());
        return dto;
    }

    private BlogPost convertToEntity(BlogPostDTO dto) {
        BlogPost entity = new BlogPost();
        if (dto.getId() != null) {
            entity = blogPostRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Blog post not found"));
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setAuthorName(dto.getAuthorName());
        return entity;
    }

    public void deleteBlogPost(UUID id) {
        blogPostRepository.deleteById(id);
    }

    public List<BlogPostDTO> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostRepository.findAllByOrderByCreatedAtDesc();
        return blogPosts.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}