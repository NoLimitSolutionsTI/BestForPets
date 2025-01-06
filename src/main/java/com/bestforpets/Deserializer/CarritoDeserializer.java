package com.bestforpets.Deserializer;

import com.bestforpets.Model.Carrito.Carrito;
import com.bestforpets.Repository.Carrito.CarritoRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class CarritoDeserializer extends JsonDeserializer<Carrito>{
    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public Carrito deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        Long id = p.getLongValue();
        return carritoRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id: " + id));
    }
}
