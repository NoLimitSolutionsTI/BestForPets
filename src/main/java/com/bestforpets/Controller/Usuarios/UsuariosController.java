package com.bestforpets.Controller.Usuarios;

import com.bestforpets.Dto.LoginDto;
import com.bestforpets.Model.Usuarios.Usuarios;
import com.bestforpets.Repository.Usuarios.UsuariosRepository;
import com.bestforpets.Security.JWTService;
import com.bestforpets.Security.LoginResponse;
import com.bestforpets.Service.Usuarios.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
public class UsuariosController {
    private final UsuariosService usuariosService;
    private final UsuariosRepository usuariosRepository;
    private final JWTService jwtService;

    public UsuariosController(UsuariosService usuariosService,
                              UsuariosRepository usuariosRepository,
                              JWTService jwtService) {
        this.usuariosService = usuariosService;
        this.usuariosRepository = usuariosRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/personal")
    public ResponseEntity<String> registerPersonal(
            @RequestBody Usuarios usuarios
    ) {
        try {
            Usuarios user = new Usuarios();

            if (usuarios.getNames() != null) {
                user.setNames(usuarios.getNames());
            }
            if (usuarios.getLastName() != null) {
                user.setLastName(usuarios.getLastName());
            }
            if (usuarios.getIdentificationNumber() != null) {
                user.setIdentificationNumber(usuarios.getIdentificationNumber());
            }
            if (usuarios.getTelephone() != null) {
                user.setTelephone(usuarios.getTelephone());
            }

            user.setEmail(usuarios.getEmail());
            user.setPassword(usuarios.getPassword());

            usuariosService.registerUser(user);
            return new ResponseEntity<>("Code sent, check your email.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPersonal(@RequestParam String email, @RequestParam String code) {
        try {
            boolean verified = usuariosService.verifyPersonal(email, code);
            return verified ? ResponseEntity.ok("Personal verified successfully.") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed or code expired.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying personal.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            try {
                Usuarios authenticatedPersonal = usuariosService.authenticate(loginDto);
                String jwtToken = jwtService.generateToken(authenticatedPersonal);
                LoginResponse loginResponse = new LoginResponse(jwtToken, null);
                return ResponseEntity.ok(loginResponse);
            } catch (RuntimeException e) {
                System.out.println("No se encontró el personal.");
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            jwtService.invalidateToken(token);
            return ResponseEntity.ok("Logged out successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Logout failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            usuariosService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error sending password reset email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            usuariosService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error resetting password.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
