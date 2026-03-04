package com.example.Application.security.controller;

import com.example.Application.security.model.Role;
import com.example.Application.security.model.Usuario;
import com.example.Application.security.repo.UsuarioRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@Validated
public class AuthPageController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthPageController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "signup"; // templates/signup.html
    }

    @PostMapping("/signup")
    public String doSignup(@RequestParam @NotBlank String username,
                           @RequestParam @NotBlank String password) {

        if (usuarioRepository.existsByUsername(username)) {
            return "redirect:/signup?error=exists";
        }

        Usuario u = new Usuario(username, passwordEncoder.encode(password), Set.of(Role.ROLE_USER));
        usuarioRepository.save(u);

        return "redirect:/login?signup=ok";
    }
}

