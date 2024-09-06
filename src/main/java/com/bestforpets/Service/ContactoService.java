package com.bestforpets.Service;

import com.bestforpets.Model.Contacto;
import com.bestforpets.Repository.ContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactoService {
    private final ContactoRepository contactoRepository;
    private final MailService mailService;

    public ContactoService(ContactoRepository contactoRepository,
                           MailService mailService) {
        this.contactoRepository = contactoRepository;
        this.mailService = mailService;
    }

    public Contacto createContact(Contacto contacto) {
        Contacto savedContact = contactoRepository.save(contacto);

        String[] recipientEmails = {"enzomartinez2412@gmail.com", "antholeca45@gmail.com"};
        mailService.sendContactEmail(recipientEmails, savedContact);

        return savedContact;
    }

    public List<Contacto> getAllContacts() {
        return contactoRepository.findAll();
    }

    public void deleteContactById(Long idContacto) {
        contactoRepository.deleteById(idContacto);
    }

    public Optional<Contacto> getContactById(Long idContacto) {
        return contactoRepository.findById(idContacto);
    }
}
