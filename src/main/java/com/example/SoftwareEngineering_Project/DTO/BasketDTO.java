package com.example.SoftwareEngineering_Project.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketDTO {
    private Long id;
    private Long userId;
    private Long productId;
}
