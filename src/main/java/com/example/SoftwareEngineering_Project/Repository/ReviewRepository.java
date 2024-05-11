package com.example.SoftwareEngineering_Project.Repository;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProduct(ProductEntity product);
}