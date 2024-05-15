package com.example.SoftwareEngineering_Project.Controller;

import com.example.SoftwareEngineering_Project.DTO.ProductMessageDTO;
import com.example.SoftwareEngineering_Project.Service.ProductMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-messages")
@RequiredArgsConstructor
public class ProductMessageController {
    private final ProductMessageService productMessageService;

    @PostMapping
    public ResponseEntity<ProductMessageDTO> createProductMessage(@RequestBody ProductMessageDTO productMessageDTO) {
        ProductMessageDTO createdProductMessage = productMessageService.createProductMessage(productMessageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductMessage);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesByProductId(@PathVariable Long productId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesByProductId(productId);
        return ResponseEntity.ok(productMessages);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesBySenderId(@PathVariable Long senderId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesBySenderId(senderId);
        return ResponseEntity.ok(productMessages);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesByReceiverId(@PathVariable Long receiverId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesByReceiverId(receiverId);
        return ResponseEntity.ok(productMessages);
    }

    @MessageMapping("/product-message.send")
    @SendTo("/topic/product-messages")
    public ProductMessageDTO sendProductMessage(@Payload ProductMessageDTO productMessageDTO) {
        return productMessageService.createProductMessage(productMessageDTO);
    }
}