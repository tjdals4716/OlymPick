package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.ProductEntity;
import com.example.SoftwareEngineering_Project.Entity.ReviewEntity;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

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
    private int likes = 0;
    private LocalDateTime statusDateTime;
    private Long userId;
    private Long productId;

    public static ReviewDTO entityToDto(ReviewEntity reviewEntity) {
        return new ReviewDTO(
                reviewEntity.getId(),
                reviewEntity.getTitle(),
                reviewEntity.getContent(),
                reviewEntity.getImage(),
                reviewEntity.getLikes(),
                reviewEntity.getStatusDateTime(),
                reviewEntity.getUser().getId(),
                reviewEntity.getProduct().getId()
        );
    }

    public ReviewEntity dtoToEntity(UserEntity user, ProductEntity product){
        return new ReviewEntity(id, title, content, image, likes, statusDateTime, user, product);
    }
}

