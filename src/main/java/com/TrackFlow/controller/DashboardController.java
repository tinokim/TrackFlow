package com.TrackFlow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.TrackFlow.model.User;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.warn("인증되지 않은 사용자가 대시보드에 접근 시도");
            return "redirect:/users/login";
        }
        model.addAttribute("user", user);
        logger.info("대시보드 접근: {}", user.getUsername());
        return "dashboard";
    }
}