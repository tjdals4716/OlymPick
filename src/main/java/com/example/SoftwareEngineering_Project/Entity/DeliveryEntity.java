package com.example.SoftwareEngineering_Project.Entity;

import com.example.SoftwareEngineering_Project.Enum.DeliveryStatus;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "delivery")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    private BasketEntity basket;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime statusDateTime;
}
