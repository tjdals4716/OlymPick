package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;

import java.util.List;
//
public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> searchProductsByName(String name);
}