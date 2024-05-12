package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
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
    private String image;
    private Category category;
    private Long userId;

    public static ProductDTO entityToDto(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getContent(),
                productEntity.getPrice(),
                productEntity.getImage(),
                productEntity.getCategory(),
                productEntity.getUser().getId()
        );
    }

    public ProductEntity dtoToEntity(UserEntity user, Category category){
        return new ProductEntity(id, name, content, price, image, category, user);
    }

}
