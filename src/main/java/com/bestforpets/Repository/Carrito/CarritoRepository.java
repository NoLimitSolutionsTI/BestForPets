package com.bestforpets.Repository.Carrito;

import com.bestforpets.Model.Carrito.Carrito;
import com.bestforpets.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuarios usuario);

}
