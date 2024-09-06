package com.bestforpets.Service.Usuarios;

import com.bestforpets.Dto.LoginDto;
import com.bestforpets.Model.Usuarios.PasswordResetToken;
import com.bestforpets.Model.Usuarios.Usuarios;
import com.bestforpets.Repository.Usuarios.PasswordResetTokenRepository;
import com.bestforpets.Repository.Usuarios.UsuariosRepository;
import com.bestforpets.Service.MailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuariosService {
    private static final Logger logger = LoggerFactory.getLogger(UsuariosService.class);

    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UsuariosService(UsuariosRepository usuariosRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           MailService mailService,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Transactional
    public void registerUser(Usuarios usuarios) {
        try {
            usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
            usuarios.setDate_created(new Date());
            usuarios.setStatus(false);
            usuarios.setFullName(usuarios.getNames() + " " + usuarios.getLastName());

            usuariosRepository.save(usuarios);
            sendVerificationCode(usuarios);
        } catch (Exception e) {
            logger.error("Error al registrar Usuario: {}", e.getMessage());
            throw new RuntimeException("Error al registrar Usuario", e);
        }
    }

    public void sendVerificationCode(Usuarios usuarios) {
        Random random = new Random();
        Usuarios usuarios1 = usuariosRepository.findById(usuarios.getIdUser())
                .orElseThrow(() -> new RuntimeException("Invalid ID User"));
        String code = String.format("%06d", random.nextInt(1000000));
        usuarios.setCode_verification(code);
        usuarios.setCode_verification_expiry(LocalDateTime.now().plusMinutes(2));
        usuariosRepository.save(usuarios);
        mailService.sendVerificationEmail(usuarios.getEmail(), code, usuarios1.getFullName());
    }

    @Transactional
    public boolean verifyPersonal(String email, String code) {
        Optional<Usuarios> personalOptional = usuariosRepository.findByEmail(email);

        if (personalOptional.isPresent()) {
            Usuarios personal = personalOptional.get();

            if (personal.getCode_verification_expiry().isBefore(LocalDateTime.now())) {
                usuariosRepository.delete(personal);
                return false;
            }

            if (personal.getCode_verification().equals(code) && personal.getCode_verification_expiry().isAfter(LocalDateTime.now())) {
                personal.setStatus(true);
                personal.setCode_verification(null);
                personal.setCode_verification_expiry(null);
                usuariosRepository.save(personal);
                mailService.sendWelcomeMessage(personal.getEmail(), personal.getFullName());
                return true;
            }
        }
        return false;
    }

    public Usuarios authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        Usuarios usuarios = usuariosRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        usuarios.setLastConnection(LocalDateTime.now());
        usuariosRepository.save(usuarios);

        return usuarios;
    }

    public void deleteUser(Long idUser) {
        usuariosRepository.deleteById(idUser);
    }

    @Value("${app.url}")
    private String appUrl;

    public void requestPasswordReset(String email) {
        Usuarios usuarios = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuarios not found"));

        PasswordResetToken token = new PasswordResetToken(usuarios);
        passwordResetTokenRepository.save(token);

        String resetLink = appUrl + "/reset-password?token=" + token.getToken();
        mailService.sendPasswordResetEmail(email, token.getToken());
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        Usuarios usuarios = resetToken.getUsuarios();

        if (usuarios.getPasswordHistory().stream()
                .anyMatch(history -> passwordEncoder.matches(newPassword, history.getPassword()))) {
            throw new RuntimeException("New password cannot be the same as a previous password");
        }

        usuarios.addPasswordToHistory(usuarios.getPassword());

        String encodedPassword = passwordEncoder.encode(newPassword);
        usuarios.setPassword(encodedPassword);
        usuariosRepository.save(usuarios);

        passwordResetTokenRepository.delete(resetToken);
    }
}
