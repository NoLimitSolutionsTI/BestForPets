package com.bestforpets.Service.Productos;

import com.bestforpets.Model.Productos.Categorias;
import com.bestforpets.Repository.Productos.CategoriasRepository;
import com.bestforpets.Repository.Productos.ProductImagesRepository;
import com.bestforpets.Repository.Productos.ProductosRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriasRepository categoriasRepository;

    public CategoriaService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public Categorias createCategory(Categorias categoria) {
        return saveCategoryRecursively(categoria);
    }

    private Categorias saveCategoryRecursively(Categorias categoria) {
        Categorias savedCategoria = categoriasRepository.save(categoria);

        for (Categorias subCategory : categoria.getSubCategories()) {
            subCategory.setParentCategory(savedCategoria);
            saveCategoryRecursively(subCategory);
        }

        return savedCategoria;
    }

    public Categorias getCategoryById(Long id) {
        return categoriasRepository.findById(id).orElse(null);
    }

    public List<Categorias> getAllCategories() {
        return categoriasRepository.findAll();
    }

    public Categorias updateCategory(Long id, Categorias updatedCategory) {
        if (categoriasRepository.existsById(id)) {
            updatedCategory.setIdCategorias(id);
            return categoriasRepository.save(updatedCategory);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        if (categoriasRepository.existsById(id)) {
            categoriasRepository.deleteById(id);
        }
    }

    public List<Categorias> getSubCategories(Long parentId) {
        Categorias parentCategory = categoriasRepository.findById(parentId).orElse(null);
        return parentCategory != null ? parentCategory.getSubCategories() : Collections.emptyList();
    }
}
