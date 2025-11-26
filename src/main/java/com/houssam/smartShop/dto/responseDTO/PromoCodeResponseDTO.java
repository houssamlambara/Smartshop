package com.houssam.smartShop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeResponseDTO {

    private String id;
    private String code;
    private Double discountPercentage;
    private Boolean active;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
