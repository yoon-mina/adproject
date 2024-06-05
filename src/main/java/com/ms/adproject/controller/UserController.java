package com.ms.adproject.controller;

import com.ms.adproject.entity.User;
import com.ms.adproject.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Timestamp;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            model.addAttribute("error", "이미 등록된 사용자입니다.");
            return "user/register";
        }
    }

}
