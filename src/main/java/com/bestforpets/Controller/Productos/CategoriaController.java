package com.bestforpets.Controller.Productos;

import com.bestforpets.Model.Productos.Categorias;
import com.bestforpets.Service.Productos.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping("/create")
    public ResponseEntity<Categorias> createCategory(@RequestBody Categorias categoria) {
        Categorias createdCategory = categoriaService.createCategory(categoria);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorias> getCategoryById(@PathVariable Long id) {
        Categorias category = categoriaService.getCategoryById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Categorias>> getAllCategories() {
        List<Categorias> categories = categoriaService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorias> updateCategory(@PathVariable Long id, @RequestBody Categorias categoria) {
        Categorias updatedCategory = categoriaService.updateCategory(id, categoria);
        return updatedCategory != null ? ResponseEntity.ok(updatedCategory) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoriaService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<Categorias>> getSubCategories(@PathVariable Long id) {
        List<Categorias> subCategories = categoriaService.getSubCategories(id);
        return ResponseEntity.ok(subCategories);
    }
}
