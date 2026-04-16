package com.wendell.backend.modules.grade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GradeAuditResponseDto(
        Long id,
        Long studentId,
        String studentName,
        Long evaluationId,
        String evaluationName,
        BigDecimal oldValue,
        BigDecimal newValue,
        String modifiedByUsername,
        LocalDateTime modificationDate
) {
}
