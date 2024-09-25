package com.TrackFlow.service;

import com.TrackFlow.dto.BlogPostDTO;
import com.TrackFlow.model.BlogPost;
import com.TrackFlow.repository.BlogPostRepository;
import com.TrackFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = convertToEntity(blogPostDTO);
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return convertToDTO(savedBlogPost);
    }

    public BlogPostDTO getBlogPostById(UUID id) {
        return blogPostRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public Page<BlogPostDTO> getAllBlogPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<BlogPostDTO> searchBlogPosts(String query, Pageable pageable) {
        return blogPostRepository.findByTitleContainingOrContentContaining(query, query, pageable)
                .map(this::convertToDTO);
    }

    public BlogPostDTO updateBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = convertToEntity(blogPostDTO);
        BlogPost updatedBlogPost = blogPostRepository.save(blogPost);
        return convertToDTO(updatedBlogPost);
    }

    public void deleteBlogPost(UUID id) {
        blogPostRepository.deleteById(id);
    }

    private BlogPostDTO convertToDTO(BlogPost blogPost) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        dto.setAuthorId(blogPost.getAuthor().getId());
        dto.setAuthorName(blogPost.getAuthor().getUsername());
        dto.setCreatedAt(blogPost.getCreatedAt());
        dto.setUpdatedAt(blogPost.getUpdatedAt());
        return dto;
    }

    private BlogPost convertToEntity(BlogPostDTO dto) {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(dto.getId());
        blogPost.setTitle(dto.getTitle());
        blogPost.setContent(dto.getContent());
        blogPost.setAuthor(userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found")));
        return blogPost;
    }
}