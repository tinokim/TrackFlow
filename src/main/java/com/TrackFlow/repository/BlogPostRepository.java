package com.TrackFlow.repository;

import com.TrackFlow.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
    Page<BlogPost> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    BlogPost findTopByOrderByCreatedAtDesc();
    List<BlogPost> findTopNByOrderByCreatedAtDesc(Pageable pageable);
    List<BlogPost> findAllByOrderByCreatedAtDesc();
}