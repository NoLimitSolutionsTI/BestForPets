package com.bestforpets.Controller.Productos;

import com.bestforpets.Model.Productos.Categorias;
import com.bestforpets.Model.Productos.ProductAttributes;
import com.bestforpets.Model.Productos.Productos;
import com.bestforpets.Service.Productos.ProductoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Productos>> getAllProducts() {
        List<Productos> products = productoService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Productos>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Productos> products = productoService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Productos>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Productos> productsPage = productoService.getProducts(pageable);
        return new ResponseEntity<>(productsPage, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Productos> createProduct(
            @RequestParam("productName") String productName,
            @RequestParam("productStock") Integer productStock,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productPrice") Double productPrice,
            @RequestParam("productStatus") Boolean productStatus,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("attributes") String attributesJson) {

        try {
            // Convert JSON string to List<ProductAttributes>
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductAttributes> attributes = objectMapper.readValue(attributesJson,
                    new TypeReference<List<ProductAttributes>>() {});

            // Create product
            Productos newProduct = new Productos();
            newProduct.setProductName(productName);
            newProduct.setProductStock(productStock);
            newProduct.setProductDescription(productDescription);
            newProduct.setProductPrice(productPrice);
            newProduct.setProductStatus(productStatus);

            // Retrieve category by ID
            Categorias category = new Categorias();
            category.setIdCategorias(categoryId);
            newProduct.setCategory(category);

            // Call service to save product and images
            Productos savedProduct = productoService.createProductWithAttributesAndImages(newProduct, files, attributes);
            return ResponseEntity.ok(savedProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
