package com.bestforpets.Model.Carrito;

import com.bestforpets.Deserializer.UsuarioDeserializer;
import com.bestforpets.Model.Usuarios.Usuarios;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Data
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario")
    @JsonDeserialize(using = UsuarioDeserializer.class)
    private Usuarios usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemCarrito> items = new ArrayList<>();

}
