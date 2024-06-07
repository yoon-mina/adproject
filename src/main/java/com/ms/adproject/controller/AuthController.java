package com.ms.adproject.controller;

import com.ms.adproject.entity.User;
import com.ms.adproject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그인 폼을 표시하는 메서드
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    // 로그인 처리를 담당하는 메서드
    @PostMapping("/login")
    public String login(@RequestParam String userid, @RequestParam String password, Model model, HttpSession session) {
        User user = userRepository.findByUserid(userid);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/movies";
        } else {
            model.addAttribute("error", "사용자 ID 또는 비밀번호를 확인해주세요.");
            return "user/login";
        }
    }

    // 로그아웃 처리를 담당하는 메서드
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
