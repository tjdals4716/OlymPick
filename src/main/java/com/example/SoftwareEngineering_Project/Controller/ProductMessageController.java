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

    //쪽지 전송
    @PostMapping
    public ResponseEntity<ProductMessageDTO> createProductMessage(@RequestBody ProductMessageDTO productMessageDTO) {
        ProductMessageDTO createdProductMessage = productMessageService.createProductMessage(productMessageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductMessage);
    }

    //특정 상품과 관련된 쪽지 목록 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesByProductId(@PathVariable Long productId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesByProductId(productId);
        return ResponseEntity.ok(productMessages);
    }

    //특정 발신자가 보낸 쪽지 목록 조회
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesBySenderId(@PathVariable Long senderId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesBySenderId(senderId);
        return ResponseEntity.ok(productMessages);
    }

    //특정 수신자가 받은 쪽지 목록 조회
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<ProductMessageDTO>> getProductMessagesByReceiverId(@PathVariable Long receiverId) {
        List<ProductMessageDTO> productMessages = productMessageService.getProductMessagesByReceiverId(receiverId);
        return ResponseEntity.ok(productMessages);
    }

    //웹소켓 사용해서 메시지 처리
    @MessageMapping("/product-message.send")
    @SendTo("/topic/product-messages")
    public ProductMessageDTO sendProductMessage(@Payload ProductMessageDTO productMessageDTO) {
        return productMessageService.createProductMessage(productMessageDTO);
    }
}