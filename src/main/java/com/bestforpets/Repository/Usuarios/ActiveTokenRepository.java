package com.bestforpets.Repository.Usuarios;

import com.bestforpets.Model.Usuarios.ActiveToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveTokenRepository extends JpaRepository<ActiveToken, Long> {
    Optional<ActiveToken> findByToken(String token);
    void deleteByToken(String token);
}
