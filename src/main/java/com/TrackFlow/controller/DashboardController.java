package com.TrackFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.TrackFlow.dto.BlogPostDTO;
import com.TrackFlow.model.BlogPost;
import com.TrackFlow.model.User;
import com.TrackFlow.service.BlogPostService; // Assuming this import is needed

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final BlogPostService blogPostService;

    @Autowired
    public DashboardController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/dashboard") // 이 URL 매핑이 올바른지 확인
    public String dashboard(HttpSession session, Model model) {
        logger.debug("Dashboard method called");
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", "admin".equals(user.getUsername()));
            logger.info("Logged in user accessing dashboard: {}", user.getUsername());
        } else {
            logger.info("Anonymous user accessing dashboard");
        }
        
        List<BlogPostDTO> allBlogPosts = blogPostService.getAllBlogPosts();
        model.addAttribute("blogPosts", allBlogPosts);
        
        return "dashboard";
    }
}