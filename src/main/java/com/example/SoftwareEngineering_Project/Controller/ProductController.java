package com.example.SoftwareEngineering_Project.Controller;

import com.example.SoftwareEngineering_Project.DTO.BasketDTO;
import com.example.SoftwareEngineering_Project.DTO.DeliveryDTO;
import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    //상품 장바구니에 담기, 동일한 상품일 경우 개수만 증가
    @PostMapping("/basket/{userId}/{productId}")
    public ResponseEntity<BasketDTO> addToBasket(@PathVariable Long productId, @PathVariable Long userId) {
        BasketDTO basketDTO = productService.addToBasket(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(basketDTO);
    }

    //상품 장바구니에서 빼기, 동일한 상품일 경우 개수만 감소, 1에서 뺄 경우 장바구니에서 상품 삭제
    @DeleteMapping("/basket/{userId}/{productId}")
    public ResponseEntity<BasketDTO> removeFromBasket(@PathVariable Long productId, @PathVariable Long userId) {
        BasketDTO basketDTO = productService.removeFromBasket(userId, productId);
        if (basketDTO != null) {
            return ResponseEntity.ok(basketDTO);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    //장바구니에 있는 상품 배송
    @PostMapping("/delivery/{basketId}")
    public ResponseEntity<DeliveryDTO> createDelivery(@PathVariable Long basketId, @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO delivery = productService.createDelivery(basketId, deliveryDTO.getStatus());
        return ResponseEntity.ok(delivery);
    }

    //배송 상태 수정
    @PutMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(@PathVariable Long deliveryId, @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = productService.updateDeliveryStatus(deliveryId, deliveryDTO.getStatus());
        return ResponseEntity.ok(updatedDelivery);
    }

    //모든 상품 조회
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    //해당 상품만 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    //카테고리별 상품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    //상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam("name") String name) {
        List<ProductDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
}