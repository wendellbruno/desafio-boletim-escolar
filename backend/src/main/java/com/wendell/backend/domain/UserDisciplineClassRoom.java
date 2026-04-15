package com.wendell.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "USER_DISCIPLINE_CLASSROOM")
@Data
public class UserDisciplineClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean active;
}