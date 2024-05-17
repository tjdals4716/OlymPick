package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Enum.BasketStatus;
import com.example.SoftwareEngineering_Project.Enum.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String content;
    private String price;
    private Long quantity;
    private String mediaUrl;
    private Category category;
    private Long userId;
    private UserDTO user;

    public static ProductDTO entityToDto(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getContent(),
                productEntity.getPrice(),
                productEntity.getQuantity(),
                productEntity.getMediaUrl(),
                productEntity.getCategory(),
                productEntity.getUser().getId(),
                UserDTO.entityToDto(productEntity.getUser())
        );
    }

    public ProductEntity dtoToEntity(UserEntity user, Category category){
        return new ProductEntity(id, name, content, price, quantity, mediaUrl, category, user);
    }

}
