package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
//
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + productDTO.getUserId()));

        ProductEntity productEntity = productDTO.dtoToEntity(userEntity);
        ProductEntity savedProduct = productRepository.save(productEntity);
        logger.info("상품 등록 완료! " + savedProduct);
        return ProductDTO.entityToDto(savedProduct);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));
        return ProductDTO.entityToDto(productEntity);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + productDTO.getUserId()));

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setContent(productDTO.getContent());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setImage(productDTO.getImage());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setUser(userEntity);

        ProductEntity updatedProduct = productRepository.save(existingProduct);
        logger.info("상품 정보 업데이트 완료! " + updatedProduct);
        return ProductDTO.entityToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id: " + id));

        productRepository.delete(productEntity);
        logger.info("상품 삭제 완료! id: " + id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProductsByName(String name) {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }
}