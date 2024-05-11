package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.BasketEntity;
import com.example.SoftwareEngineering_Project.Entity.DeliveryEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryDTO {
    private Long id;
    private Long basketId;
    private DeliveryStatus status;
    private LocalDateTime statusDateTime;

    public static DeliveryDTO entityToDto(DeliveryEntity deliveryEntity) {
        return new DeliveryDTO(
                deliveryEntity.getId(),
                deliveryEntity.getBasket().getId(),
                deliveryEntity.getStatus(),
                deliveryEntity.getStatusDateTime()
        );
    }

    public DeliveryEntity dtoToEntity(BasketEntity basket, DeliveryStatus status){
        return new DeliveryEntity(id, basket, status, statusDateTime);
    }
}
