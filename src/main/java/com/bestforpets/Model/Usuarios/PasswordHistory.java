package com.bestforpets.Model.Usuarios;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_history")
@Data
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuarios usuarios;

    @Column(nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();
}
