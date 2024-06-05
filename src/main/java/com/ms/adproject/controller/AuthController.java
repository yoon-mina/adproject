package com.ms.adproject.controller;

import com.ms.adproject.entity.User;
import com.ms.adproject.repository.UserRepository;
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

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userid, @RequestParam String password, Model model) {
        User user = userRepository.findByUserid(userid);
        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공
            return "redirect:/movies";
        } else {
            // 로그인 실패
            model.addAttribute("error", "사용자 ID 또는 비밀번호를 확인해주세요.");
            return "user/login";
        }
    }
}
