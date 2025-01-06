package com.bestforpets.Model.Carrito;

import com.bestforpets.Deserializer.CarritoDeserializer;
import com.bestforpets.Deserializer.ProductosDeserializer;
import com.bestforpets.Model.Productos.Productos;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "itemCarrito")
@Data
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto")
    @JsonDeserialize(using = ProductosDeserializer.class)
    private Productos producto;

    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonDeserialize(using = CarritoDeserializer.class)
    private Carrito carrito;

    public double getSubTotal() {
        return producto.getProductPrice() * cantidad;
    }
}
