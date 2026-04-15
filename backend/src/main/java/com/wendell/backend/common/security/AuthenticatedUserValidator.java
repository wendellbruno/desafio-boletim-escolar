package com.wendell.backend.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wendell.backend.common.dto.AuthenticatedUserDto;
import com.wendell.backend.common.exception.UnauthorizedException;

@Component
public class AuthenticatedUserValidator {

    public AuthenticatedUserDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getDetails() instanceof AuthenticatedUserDto authenticatedUser)) {
            throw new UnauthorizedException("Usuario nao autenticado");
        }

        return authenticatedUser;
    }

    public void validateUserId(Long userId) {
        AuthenticatedUserDto authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.userId().equals(userId)) {
            throw new UnauthorizedException("Usuario nao pode acessar dados de outro usuario");
        }
    }

    public void validateClassroomId(Long classroomId) {
        AuthenticatedUserDto authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.classrooms().contains(classroomId)) {
            throw new UnauthorizedException("Usuario nao possui acesso a esta sala");
        }
    }

    public void validateDisciplineId(Long disciplineId) {
        AuthenticatedUserDto authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.disciplines().contains(disciplineId)) {
            throw new UnauthorizedException("Usuario nao possui acesso a esta disciplina");
        }
    }
}
