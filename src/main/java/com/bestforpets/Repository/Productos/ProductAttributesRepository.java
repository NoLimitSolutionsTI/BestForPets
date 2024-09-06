package com.bestforpets.Repository.Productos;

import com.bestforpets.Model.Productos.ProductAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, Long>  {
}
