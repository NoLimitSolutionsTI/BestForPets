package com.bestforpets.Model.Productos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorias;

    private String categoriasName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    @JsonIgnore
    private Categorias parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Categorias> subCategories;
}
