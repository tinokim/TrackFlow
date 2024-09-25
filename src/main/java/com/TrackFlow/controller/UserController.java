package com.TrackFlow.controller;

import com.TrackFlow.model.User;
import com.TrackFlow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            userService.registerUser(username, email, password);
            return "redirect:/users/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session, Model model) {
        try {
            User user = userService.findByUsername(username);
            session.setAttribute("user", user);
            logger.info("User {} logged in successfully. Redirecting to dashboard.", username);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            logger.error("Login failed for user: {}. Error: {}", username, e.getMessage());
            model.addAttribute("error", "존재하지 않는 사용자 이름입니다.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/dashboard";
    }
}