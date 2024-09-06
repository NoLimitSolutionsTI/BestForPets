package com.bestforpets.Model.Productos;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products_attributes")
@Data
public class ProductAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAttribute;

    private String attributeName;
    private String attributeValue;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Productos product;
}
