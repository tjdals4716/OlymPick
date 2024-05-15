package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.ProductMessageDTO;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;

import java.util.List;

public interface ProductMessageService {
    ProductMessageDTO createProductMessage(ProductMessageDTO productMessageDTO);
    List<ProductMessageDTO> getProductMessagesByProductId(Long productId);
    List<ProductMessageDTO> getProductMessagesBySenderId(Long senderId);
    List<ProductMessageDTO> getProductMessagesByReceiverId(Long receiverId);
}