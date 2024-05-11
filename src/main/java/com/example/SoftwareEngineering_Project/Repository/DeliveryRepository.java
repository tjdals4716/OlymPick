package com.example.SoftwareEngineering_Project.Repository;

import com.example.SoftwareEngineering_Project.Entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
}