package com.example.SoftwareEngineering_Project.Entity;

import com.example.SoftwareEngineering_Project.Enum.Category;
import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;
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
    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
