package com.bestforpets.Deserializer;

import com.bestforpets.Model.Productos.Productos;
import com.bestforpets.Repository.Productos.ProductosRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ProductosDeserializer extends JsonDeserializer<Productos> {
    @Autowired
    private ProductosRepository productosRepository;

    @Override
    public Productos deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        Long id = p.getLongValue();
        return productosRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id: " + id));
    }
}
