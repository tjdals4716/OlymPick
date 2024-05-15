package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ProductMessageEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMessageDTO {
    private Long id;
    private String content;
    private LocalDateTime sendTime;
    private Long sender;
    private Long receiver;
    private Long productId;

    public static ProductMessageDTO entityToDto(ProductMessageEntity productMessageEntity){
        return new ProductMessageDTO(
                productMessageEntity.getId(),
                productMessageEntity.getContent(),
                productMessageEntity.getSendTime(),
                productMessageEntity.getSender().getId(),
                productMessageEntity.getReceiver().getId(),
                productMessageEntity.getProduct().getId()
        );
    }

    public ProductMessageEntity dtoToEntity(UserEntity sender, UserEntity receiver, ProductEntity product){
        return new ProductMessageEntity(id, content, sendTime, sender, receiver, product);
    }
}
