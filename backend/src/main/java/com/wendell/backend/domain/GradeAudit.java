package com.wendell.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "grade_audit")
public class GradeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "old_value")
    private BigDecimal oldValue;

    @Column(name = "new_value")
    private BigDecimal newValue;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;
}