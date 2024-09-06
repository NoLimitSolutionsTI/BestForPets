package com.bestforpets.Repository.Productos;

import com.bestforpets.Model.Productos.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Long> {
}
