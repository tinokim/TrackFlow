package com.TrackFlow.controller;

import com.TrackFlow.dto.BlogPostDTO;
import com.TrackFlow.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping
    public ResponseEntity<BlogPostDTO> createBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        return ResponseEntity.ok(blogPostService.createBlogPost(blogPostDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getBlogPostById(@PathVariable UUID id) {
        BlogPostDTO blogPostDTO = blogPostService.getBlogPostById(id);
        if (blogPostDTO != null) {
            return ResponseEntity.ok(blogPostDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostDTO>> getAllBlogPosts(Pageable pageable) {
        return ResponseEntity.ok(blogPostService.getAllBlogPosts(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlogPostDTO>> searchBlogPosts(@RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok(blogPostService.searchBlogPosts(query, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDTO> updateBlogPost(@PathVariable UUID id, @RequestBody BlogPostDTO blogPostDTO) {
        blogPostDTO.setId(id);
        return ResponseEntity.ok(blogPostService.updateBlogPost(blogPostDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable UUID id) {
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.noContent().build();
    }
}