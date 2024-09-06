package com.bestforpets.Security;

import com.bestforpets.Model.Usuarios.CustomUserDetails;
import com.bestforpets.Model.Usuarios.Usuarios;
import com.bestforpets.Repository.Usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class ApplicationConfiguration {
    private final UsuariosRepository usuariosRepository;

    @Autowired
    public ApplicationConfiguration(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Usuarios> usuario = usuariosRepository.findByEmail(username);
            if (usuario.isPresent()) {
                Usuarios usuarios = usuario.get();
                return new CustomUserDetails(
                        usuarios,
                        usuarios.getEmail(),
                        usuarios.getPassword(),
                        usuarios.getAuthorities()
                );
            }

            throw new UsernameNotFoundException("Username not found");
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
