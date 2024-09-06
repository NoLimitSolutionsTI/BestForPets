package com.bestforpets.Service.Productos;

import com.bestforpets.Model.Productos.ProductAttributes;
import com.bestforpets.Model.Productos.ProductImages;
import com.bestforpets.Model.Productos.Productos;
import com.bestforpets.Repository.Productos.CategoriasRepository;
import com.bestforpets.Repository.Productos.ProductAttributesRepository;
import com.bestforpets.Repository.Productos.ProductImagesRepository;
import com.bestforpets.Repository.Productos.ProductosRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductoService {
    private final ProductosRepository productosRepository;
    private final CategoriasRepository categoriasRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductAttributesRepository productAttributesRepository;

    public ProductoService(ProductosRepository productosRepository, CategoriasRepository categoriasRepository,
                           ProductImagesRepository productImagesRepository, ProductAttributesRepository productAttributesRepository) {
        this.productosRepository = productosRepository;
        this.categoriasRepository = categoriasRepository;
        this.productImagesRepository = productImagesRepository;
        this.productAttributesRepository = productAttributesRepository;
    }

    public List<Productos> getAllProducts() {
        return productosRepository.findAll();
    }

    public List<Productos> getProductsByCategory(Long categoryId) {
        return productosRepository.findByCategory_IdCategorias(categoryId);
    }

    public Page<Productos> getProducts(Pageable pageable) {
        return productosRepository.findAll(pageable);
    }

    public Productos createProductWithAttributesAndImages(Productos product, List<MultipartFile> imageFiles, List<ProductAttributes> attributes) throws IOException {
        Productos savedProduct = productosRepository.save(product);

        for (ProductAttributes attribute : attributes) {
            attribute.setProduct(savedProduct);
            productAttributesRepository.save(attribute);
        }

        for (MultipartFile file : imageFiles) {
            String imageUrl = storeImage(file);
            ProductImages productImage = new ProductImages();
            productImage.setProduct(savedProduct);
            productImage.setImageUrl(imageUrl);
            productImagesRepository.save(productImage);
        }

        return savedProduct;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    private String storeImage(MultipartFile file) throws IOException {
        // Ruta del directorio de carga dentro del proyecto
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/uploads");

        File directory = uploadDir.toFile();

        // Crear el directorio si no existe
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generar un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File uploadFile = new File(directory, fileName);

        // Guardar el archivo
        file.transferTo(uploadFile);

        // Retornar la ruta del archivo en el sistema
        return uploadFile.getAbsolutePath(); // Retorna la ruta completa del archivo
    }
}
