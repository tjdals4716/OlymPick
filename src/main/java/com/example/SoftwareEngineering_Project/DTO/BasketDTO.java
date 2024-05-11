package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.BasketEntity;
import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketDTO {
    private Long id;
    private int count;
    private Long userId;
    private Long productId;

    public static BasketDTO entityToDto(BasketEntity basketEntity) {
        return new BasketDTO(
                basketEntity.getId(),
                basketEntity.getCount(),
                basketEntity.getUser().getId(),
                basketEntity.getProduct().getId()
        );
    }

    public BasketEntity dtoToEntity(UserEntity user, ProductEntity product) {
        return new BasketEntity(id, count, user, product);
    }
}
