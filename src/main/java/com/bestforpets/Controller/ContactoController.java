package com.bestforpets.Controller;

import com.bestforpets.Model.Contacto;
import com.bestforpets.Service.ContactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacto")
public class ContactoController {
    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody Contacto contacto) {
        contactoService.createContact(contacto);
        return ResponseEntity.ok("Contacto registrado y correo enviado exitosamente.");
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> getAllContacts() {
        List<Contacto> contacts = contactoService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        contactoService.deleteContactById(id);
        return ResponseEntity.ok("Contacto eliminado exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contacto> getContactById(@PathVariable Long id) {
        return contactoService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
