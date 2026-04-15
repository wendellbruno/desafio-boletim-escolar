package com.wendell.backend.common.dto;

import java.util.List;

public record AuthenticatedUserDto(
        String username,
        Long userId,
        List<Long> classrooms,
        List<Long> disciplines) {
}
