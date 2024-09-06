package com.bestforpets.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contact")
@Data
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContacto;

    @Column(nullable = false)
    private String contactoEmail;

    @Column(nullable = false)
    private String contactoDescription;

    @Column(nullable = false)
    private String contactoTelephone;
}
