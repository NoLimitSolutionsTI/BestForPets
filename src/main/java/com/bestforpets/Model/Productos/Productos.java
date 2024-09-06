package com.bestforpets.Model.Productos;

import com.bestforpets.Deserializer.CategoriasDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    private String productName;

    private Integer productStock;

    private String productDescription;

    private Double productPrice;

    private Boolean productStatus;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    @JsonDeserialize(using = CategoriasDeserializer.class)
    private Categorias category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductAttributes> productAttributes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImages> productImages;
}
