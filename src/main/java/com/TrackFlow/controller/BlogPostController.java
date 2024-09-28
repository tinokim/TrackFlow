package com.TrackFlow.controller;

import com.TrackFlow.dto.BlogPostDTO;
import com.TrackFlow.model.User;
import com.TrackFlow.service.BlogPostService;
import com.TrackFlow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final UserService userService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, UserService userService) {
        this.blogPostService = blogPostService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getBlogPostById(@PathVariable UUID id, Model model, HttpSession session) {
        BlogPostDTO blogPostDTO = blogPostService.getBlogPostById(id);
        if (blogPostDTO != null) {
            model.addAttribute("blogPost", blogPostDTO);
            User user = (User) session.getAttribute("user");
            boolean isAdmin = user != null && "admin".equals(user.getUsername());
            model.addAttribute("isAdmin", isAdmin);
            return "blog-post-detail";
        } else {
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && userService.isAdmin(user.getId())) {
            BlogPostDTO blogPostDTO = blogPostService.getBlogPostById(id);
            if (blogPostDTO != null) {
                model.addAttribute("blogPost", blogPostDTO);
                return "blog-post-edit";
            }
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/edit")
    public String updateBlogPost(@PathVariable UUID id, @ModelAttribute BlogPostDTO blogPostDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getUsername())) {
            blogPostDTO.setId(id);
            blogPostService.updateBlogPost(blogPostDTO);
        }
        return "redirect:/blog-posts/" + id;
    }

    @GetMapping("/new")
    public String newBlogPostForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getUsername())) {
            model.addAttribute("blogPost", new BlogPostDTO());
            return "blog-post-form";
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/new")
    public String createBlogPost(@ModelAttribute BlogPostDTO blogPostDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getUsername())) {
            BlogPostDTO createdPost = blogPostService.createBlogPost(blogPostDTO, user.getUsername());
            return "redirect:/blog-posts/" + createdPost.getId();
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String deleteBlogPost(@PathVariable UUID id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getUsername())) {
            blogPostService.deleteBlogPost(id);
            return "redirect:/dashboard";
        }
        return "redirect:/blog-posts/" + id;
    }
}