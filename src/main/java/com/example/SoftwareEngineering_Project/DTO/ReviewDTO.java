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
    private String mediaUrl;
    private int likes = 0;
    private LocalDateTime statusDateTime;
    private Long userId;
    private Long productId;

    public static ReviewDTO entityToDto(ReviewEntity reviewEntity) {
        return new ReviewDTO(
                reviewEntity.getId(),
                reviewEntity.getTitle(),
                reviewEntity.getContent(),
                reviewEntity.getMediaUrl(),
                reviewEntity.getLikes(),
                reviewEntity.getStatusDateTime(),
                reviewEntity.getUser().getId(),
                reviewEntity.getProduct().getId()
        );
    }

    public ReviewEntity dtoToEntity(UserEntity user, ProductEntity product){
        return new ReviewEntity(id, title, content, mediaUrl, likes, statusDateTime, user, product);
    }
}

