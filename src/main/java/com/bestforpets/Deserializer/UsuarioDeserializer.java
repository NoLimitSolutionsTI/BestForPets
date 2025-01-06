package com.bestforpets.Deserializer;

import com.bestforpets.Model.Usuarios.Usuarios;
import com.bestforpets.Repository.Usuarios.UsuariosRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UsuarioDeserializer extends JsonDeserializer<Usuarios> {
   @Autowired
   private UsuariosRepository usuariosRepository;

    @Override
    public Usuarios deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        Long id = p.getLongValue();
        return usuariosRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id: " + id));

    }
}
