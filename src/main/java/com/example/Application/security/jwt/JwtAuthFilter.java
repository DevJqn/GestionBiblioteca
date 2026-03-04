package com.example.Application.security.jwt;

import com.example.Application.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Obtener cabecera Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Validar formato "Bearer token"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Quitar "Bearer "
        
        // 3. Extraer username y validar
        username = jwtUtils.getUsernameFromToken(jwt);

        // 4. Si hay usuario y no hay autenticación previa en el contexto actual
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Cargar usuario de BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Validar token
            if (jwtUtils.validateToken(jwt)) {
                
                // Crear objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No guardamos credenciales (password) en memoria
                        userDetails.getAuthorities()
                );

                // Añadir detalles de la petición
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ESTABLECER AUTENTICACIÓN FINAL
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Continuar la cadena
        filterChain.doFilter(request, response);
    }
}

