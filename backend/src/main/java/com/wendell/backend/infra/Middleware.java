package com.wendell.backend.infra;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wendell.backend.common.dto.ExceptionDTO;
import com.wendell.backend.modules.user.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Middleware extends OncePerRequestFilter {

    private final JwtService tokenService;
    private final ObjectMapper objectMapper;

    public Middleware(JwtService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String reqUrl = request.getRequestURI();
        return "/user/login".equals(reqUrl)
                || "/user/login/".equals(reqUrl)
                || reqUrl.startsWith("/swagger-ui")
                || reqUrl.startsWith("/v3/api-docs")
                || reqUrl.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoveryToken(request);
        if (token == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token nao informado");
            return;
        }

        if (!tokenService.validateToken(token)) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token invalido");
            return;
        }

        String username = tokenService.getUsernameFromToken(token);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7).trim();
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionDTO(message)));
    }
}
