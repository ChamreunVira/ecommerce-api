package com.kh.vira_dev.ecommerceapi.filter;

import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.security.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserDetailsService userDetailsService;

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String userEmail = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }

        if(jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userEmail = jwtService.extractEmail(jwtToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwtToken , userDetails)) {
                UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                new WebAuthenticationDetailsSource().buildDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
