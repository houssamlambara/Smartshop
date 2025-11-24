package com.houssam.smartShop.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeRequestDTO {

    @NotBlank(message = "Le code promo est obligatoire")
    private String code;

    @NotNull(message = "Le pourcentage de remise est obligatoire")
    @Positive(message = "Le pourcentage de remise doit être positif")
    private Double discountPercentage;

    private Boolean active;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
