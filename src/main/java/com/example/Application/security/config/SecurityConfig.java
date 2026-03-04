package com.example.Application.security.config;

import com.example.Application.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Para poder usar @PreAuthorize en los controladores
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // 1) API REST: JWT + stateless
    @Bean
    @Order(1) // Esta cadena tiene prioridad
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**") // Solo para rutas /api/**
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para API REST
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Público
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        
                        // Reglas por recurso para tu biblioteca
                        .requestMatchers(HttpMethod.GET, "/api/libros/**", "/api/socios/**", "/api/prestamos/**")
                            .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        
                        .requestMatchers(HttpMethod.POST, "/api/libros/**", "/api/socios/**", "/api/prestamos/**")
                            .hasAuthority("ROLE_ADMIN")
                        
                        .requestMatchers(HttpMethod.PUT, "/api/libros/**", "/api/socios/**", "/api/prestamos/**")
                            .hasAuthority("ROLE_ADMIN")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/libros/**", "/api/socios/**", "/api/prestamos/**")
                            .hasAuthority("ROLE_ADMIN")
                        
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults()) // opcional para depurar
                .build();
    }

    // 2) Web MVC: formLogin + sesión
    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**") // Todo lo que NO sea /api/**
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/signup", 
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()
                        
                        // Si tuvieras rutas de admin en MVC (opcional)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

