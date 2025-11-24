package com.houssam.smartShop.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {

    @NotBlank(message = "Le produitId est obligatoire")
    private String produitId;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être un nombre positif")
    private Integer quantite;
}
