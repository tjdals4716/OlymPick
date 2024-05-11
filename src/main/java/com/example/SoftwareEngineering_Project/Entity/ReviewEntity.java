package com.example.SoftwareEngineering_Project.Entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String likes;
//
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
