package com.example.SoftwareEngineering_Project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String image;
    private int likes = 0;
    private LocalDateTime statusDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToMany(mappedBy = "likedReviews")
    @JsonIgnore
    private Set<UserEntity> likedUsers = new HashSet<>();

    public ReviewEntity(Long id, String title, String content, String image, int likes,
                        LocalDateTime statusDateTime, UserEntity user, ProductEntity product) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.likes = likes;
        this.statusDateTime = statusDateTime;
        this.user = user;
        this.product = product;
    }
}
