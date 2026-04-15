package com.wendell.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal weight;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @OneToMany(mappedBy = "evaluation")
    private List<Grade> grades;

    @OneToMany(mappedBy = "evaluation")
    private List<GradeAudit> gradeAudits;

}