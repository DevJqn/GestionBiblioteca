package com.example.Application.security.init;

import com.example.Application.security.model.Role;
import com.example.Application.security.model.Usuario;
import com.example.Application.security.repo.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)
            );
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario admin creado: admin/admin123");
        }

        if (!usuarioRepository.existsByUsername("user")) {
            Usuario user = new Usuario(
                    "user",
                    passwordEncoder.encode("user123"),
                    Set.of(Role.ROLE_USER)
            );
            usuarioRepository.save(user);
            System.out.println("✅ Usuario user creado: user/user123");
        }
    }
}

