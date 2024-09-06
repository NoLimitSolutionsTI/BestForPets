package com.bestforpets.Repository.Productos;

import com.bestforpets.Model.Productos.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {
    List<Productos> findByCategory_IdCategorias(Long categoryId);

}
