package com.example.SoftwareEngineering_Project.Entity;

import com.example.SoftwareEngineering_Project.Enum.BasketStatus;
import com.example.SoftwareEngineering_Project.Enum.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String content;
    private String price;
    private Long quantity;
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
