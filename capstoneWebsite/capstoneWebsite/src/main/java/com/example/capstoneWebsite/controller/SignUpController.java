package com.example.capstoneWebsite.controller;


import com.example.capstoneWebsite.dto.UserDTO;
import com.example.capstoneWebsite.entity.User;
import com.example.capstoneWebsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }
    @GetMapping("/createAccount")
    public String createAccount(Model model) {
        model.addAttribute("user", new UserDTO());
        return "createAccount";
    }
    @PostMapping("/signup-process")
    public String signUpUser(@ModelAttribute UserDTO user) {
        userService.save(user);
        return "redirect:/login";
    }
}
