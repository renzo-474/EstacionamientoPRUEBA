package com.estacionamiento.config;

import com.estacionamiento.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        

        if (requestPath.equals("/") || 
            requestPath.endsWith(".html") || 
            requestPath.endsWith(".css") || 
            requestPath.endsWith(".js") || 
            requestPath.startsWith("/css/") || 
            requestPath.startsWith("/js/") || 
            requestPath.startsWith("/assets/") ||
            requestPath.startsWith("/api/auth/") || 
            requestPath.startsWith("/auth/")) {
            
            System.out.println("=== JWT FILTER: Saltando autenticación para recurso estático: " + requestPath + " ===");
            filterChain.doFilter(request, response);
            return;
        }
        
        System.out.println("=== JWT FILTER: Procesando: " + requestPath + " ===");
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("=== JWT FILTER: No hay token válido, continuando sin autenticación ===");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUsername(jwt);
        
        System.out.println("=== JWT FILTER: Token encontrado para usuario: " + userEmail + " ===");

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                System.out.println("=== JWT FILTER: Token válido, estableciendo autenticación ===");
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("=== JWT FILTER: Token inválido ===");
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
