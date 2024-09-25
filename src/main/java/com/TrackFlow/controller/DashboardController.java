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

    @GetMapping("/dashboard") // 이 URL 매핑이 올바른지 확인
    public String dashboard(HttpSession session, Model model) {
        logger.debug("Dashboard method called");
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            logger.info("Logged in user accessing dashboard: {}", user.getUsername());
        } else {
            logger.info("Anonymous user accessing dashboard");
        }
        // 여기에 대시보드에 표시할 데이터를 추가하세요
        return "dashboard";
    }
}