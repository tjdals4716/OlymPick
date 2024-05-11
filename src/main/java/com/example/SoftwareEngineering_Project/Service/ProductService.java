package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.BasketDTO;
import com.example.SoftwareEngineering_Project.DTO.DeliveryDTO;
import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> searchProductsByName(String name);
    BasketDTO addToBasket(Long userId, Long productId);
    BasketDTO removeFromBasket(Long userId, Long productId);
    List<DeliveryDTO> createDeliveryForBasket(Long userId, DeliveryStatus status);
    DeliveryDTO updateDeliveryStatus(Long deliveryId, DeliveryStatus status);
}