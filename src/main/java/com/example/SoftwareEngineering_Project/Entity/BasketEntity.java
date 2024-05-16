package com.example.SoftwareEngineering_Project.Entity;

import com.example.SoftwareEngineering_Project.Enum.BasketStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "basket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BasketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long count;

    @Enumerated(EnumType.STRING)
    private BasketStatus basketStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
