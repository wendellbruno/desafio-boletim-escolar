package com.wendell.backend.infra.database.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioRTurma> usuarioRTurmas;

    @OneToMany(mappedBy = "usuarioAlterou")
    private List<AuditoriaNota> auditorias;
}
