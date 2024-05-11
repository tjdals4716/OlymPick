package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDTO {
    private Long id;
    private String title;
    private String content;
    private String image;
    private int likes;
    private Long productId;

    public static ReviewDTO entityToDto(ReviewEntity reviewEntity) {
        return new ReviewDTO(
                reviewEntity.getId(),
                reviewEntity.getTitle(),
                reviewEntity.getContent(),
                reviewEntity.getImage(),
                reviewEntity.getLikes(),
                reviewEntity.getProduct().getId()
        );
    }

    public ReviewEntity dtoToEntity(ProductEntity product){
        return new ReviewEntity(id, title, content, image, likes, product);
    }
}

