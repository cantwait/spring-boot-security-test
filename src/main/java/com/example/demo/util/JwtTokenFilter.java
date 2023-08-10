package com.example.demo.util;

import java.io.IOException;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.filter.JwtTokenUtil;
import com.example.demo.repository.UserRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jtwTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header.isEmpty() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // get jwt token and validate
        final var token = header.split(" ")[1].trim();

        var username = jtwTokenUtil.getUsernameFromToken(token);

        if (Objects.isNull(username) || username.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // get user identity and set it on the spring security context
        var userDetails = userDetailsService.loadUserByUsername(token);

        if (Objects.isNull(userDetails) || !jtwTokenUtil.validate(token, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}
