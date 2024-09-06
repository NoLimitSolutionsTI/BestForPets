package com.bestforpets.Deserializer;

import com.bestforpets.Model.Productos.Categorias;
import com.bestforpets.Repository.Productos.CategoriasRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CategoriasDeserializer extends JsonDeserializer<Categorias> {

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Override
    public Categorias deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        Long id = p.getLongValue();
        return categoriasRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id: " + id));
    }
}
