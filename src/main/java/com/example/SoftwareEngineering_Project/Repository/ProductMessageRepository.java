package com.example.SoftwareEngineering_Project.Repository;

import com.example.SoftwareEngineering_Project.Entity.ProductMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMessageRepository extends JpaRepository<ProductMessageEntity, Long> {
    List<ProductMessageEntity> findByProductId(Long productId);
    List<ProductMessageEntity> findBySenderId(Long senderId);
    List<ProductMessageEntity> findByReceiverId(Long receiverId);
}