package com.example.SoftwareEngineering_Project.Controller;

import com.example.SoftwareEngineering_Project.DTO.BasketDTO;
import com.example.SoftwareEngineering_Project.DTO.DeliveryDTO;
import com.example.SoftwareEngineering_Project.DTO.ProductDTO;
import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;
import com.example.SoftwareEngineering_Project.Service.ProductService;
import com.example.SoftwareEngineering_Project.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    //상품 등록, 컨트롤러 메서드에서 @RequestBody 어노테이션을 사용할 때는 하나의 객체만 매핑할 수 있음
    @SneakyThrows
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDTO> createProduct(@RequestPart("productData") String productData, @RequestPart(value = "mediaData") MultipartFile mediaData) {
        ObjectMapper mapper = new ObjectMapper();
        ProductDTO productDTO = mapper.readValue(productData, ProductDTO.class);
        ProductDTO createdProduct = productService.createProduct(productDTO, mediaData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    //상품 장바구니에 담기, 동일한 상품일 경우 개수만 증가
    @PostMapping(value ="/basket/{userId}/{productId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasketDTO> addToBasket(@PathVariable Long productId, @PathVariable Long userId, @RequestBody Map<String, Long> requestBody) {
            Long quantity = requestBody.getOrDefault("quantity", 1L); // 기본값 1
            BasketDTO basketDTO = productService.addToBasket(userId, productId, quantity);
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

    //상품 장바구니에서 전체 개수 제거
    @PutMapping("/basket/{userId}/{productId}")
    public ResponseEntity<Void> removeAllFromBasket(@PathVariable Long userId, @PathVariable Long productId) {
        productService.removeAllFromBasket(userId, productId);
        return ResponseEntity.noContent().build();
    }


    //장바구니에 있는 전체 상품 배송
    @PostMapping("/delivery/basket")
    public ResponseEntity<List<DeliveryDTO>> createDeliveryForBasket(@RequestBody Map<String, Object> requestBody) {
        Long userId = ((Number) requestBody.get("userId")).longValue();
        String statusString = (String) requestBody.get("status");
        DeliveryStatus status = DeliveryStatus.valueOf(statusString);

        List<DeliveryDTO> createdDeliveries = productService.createDeliveryForBasket(userId, status);
        return ResponseEntity.ok(createdDeliveries);
    }

    //사용자 장바구니에 있는 상품을 선택적으로 배송
    @PostMapping("/delivery/basket/{basketId}")
    public ResponseEntity<DeliveryDTO> createDeliveryForBasketItem(@PathVariable Long basketId, @RequestBody Map<String, Object> requestBody) {
        Long userId = ((Number) requestBody.get("userId")).longValue();
        String statusString = (String) requestBody.get("status");
        DeliveryStatus status = DeliveryStatus.valueOf(statusString);

        DeliveryDTO createdDelivery = productService.createDeliveryForBasketItem(userId, basketId, status);
        return ResponseEntity.ok(createdDelivery);
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
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDTO productDTO) {
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

    //사용자 장바구니 조회
    @GetMapping("/basket/{userId}")
    public ResponseEntity<List<BasketDTO>> getUserBasket(@PathVariable Long userId) {
        List<BasketDTO> basketItems = productService.getUserBasket(userId);
        return ResponseEntity.ok(basketItems);
    }
}