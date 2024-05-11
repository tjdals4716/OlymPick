package com.example.SoftwareEngineering_Project.Repository;

import com.example.SoftwareEngineering_Project.Entity.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, Long> {
    List<BasketEntity> findByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
    BasketEntity findByUser_IdAndProduct_Id(Long userId, Long productId);
}