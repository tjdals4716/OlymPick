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
    private Long count;
    private Long userId;
    private Long basketId;
    private DeliveryStatus status;
    private LocalDateTime statusDateTime;
    private UserDTO user;

    public static DeliveryDTO entityToDto(DeliveryEntity deliveryEntity) {
        return new DeliveryDTO(
                deliveryEntity.getId(),
                deliveryEntity.getCount(),
                deliveryEntity.getUser().getId(),
                deliveryEntity.getBasket().getId(),
                deliveryEntity.getStatus(),
                deliveryEntity.getStatusDateTime(),
                UserDTO.entityToDto(deliveryEntity.getUser())
        );
    }

    public DeliveryEntity dtoToEntity(UserEntity user, BasketEntity basket, DeliveryStatus status){
        return new DeliveryEntity(id, count, user, basket, status, statusDateTime);
    }
}
