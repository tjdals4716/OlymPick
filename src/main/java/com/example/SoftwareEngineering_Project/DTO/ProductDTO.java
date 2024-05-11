package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
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
    private String likes;
    private String image;
    private String category;
    private Long userId;
//
    public static ProductDTO entityToDto(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getContent(),
                productEntity.getPrice(),
                productEntity.getLikes(),
                productEntity.getImage(),
                productEntity.getCategory(),
                productEntity.getUser().getId()
        );
    }

    public ProductEntity dtoToEntity(UserEntity user){
        return new ProductEntity(id, name, content, price, likes, image, category, user);
    }

}
