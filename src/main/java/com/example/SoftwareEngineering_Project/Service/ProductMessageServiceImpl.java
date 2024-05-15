package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.Config.WebSocketMessageHandler;
import com.example.SoftwareEngineering_Project.DTO.ProductMessageDTO;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ProductMessageEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.ProductMessageRepository;
import com.example.SoftwareEngineering_Project.Repository.ProductRepository;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMessageServiceImpl implements ProductMessageService {
    private final ProductMessageRepository productMessageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WebSocketMessageHandler webSocketMessageHandler;

    @Override
    public ProductMessageDTO createProductMessage(ProductMessageDTO productMessageDTO) {
        UserEntity sender = userRepository.findById(productMessageDTO.getSender())
                .orElseThrow(() -> new RuntimeException("발신자를 찾을 수 없습니다. userId: " + productMessageDTO.getSender()));

        UserEntity receiver = userRepository.findById(productMessageDTO.getReceiver())
                .orElseThrow(() -> new RuntimeException("수신자를 찾을 수 없습니다. userId: " + productMessageDTO.getReceiver()));

        ProductEntity product = productRepository.findById(productMessageDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. productId: " + productMessageDTO.getProductId()));

        ProductMessageEntity productMessageEntity = productMessageDTO.dtoToEntity(sender, receiver, product);
        ProductMessageEntity savedProductMessage = productMessageRepository.save(productMessageEntity);
        String receiverUsername = receiver.getUid();
        String notificationMessage = "새로운 메시지가 도착했습니다.";
        try {
            webSocketMessageHandler.sendNotification(receiverUsername, notificationMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ProductMessageDTO.entityToDto(savedProductMessage);
    }

    @Override
    public List<ProductMessageDTO> getProductMessagesByProductId(Long productId) {
        List<ProductMessageEntity> productMessageEntities = productMessageRepository.findByProductId(productId);
        return productMessageEntities.stream()
                .map(ProductMessageDTO::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductMessageDTO> getProductMessagesBySenderId(Long senderId) {
        List<ProductMessageEntity> productMessageEntities = productMessageRepository.findBySenderId(senderId);
        return productMessageEntities.stream()
                .map(ProductMessageDTO::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductMessageDTO> getProductMessagesByReceiverId(Long receiverId) {
        List<ProductMessageEntity> productMessageEntities = productMessageRepository.findByReceiverId(receiverId);
        return productMessageEntities.stream()
                .map(ProductMessageDTO::entityToDto)
                .collect(Collectors.toList());
    }
}