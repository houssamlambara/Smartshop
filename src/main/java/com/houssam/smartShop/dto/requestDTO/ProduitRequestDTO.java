package com.houssam.smartShop.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRequestDTO {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, message = "Le nom du produit doit contenir au moins 2 caractères")
    private String nom;

    private String description;

    @NotNull(message = "Le prix du produit est obligatoire")
    @Positive(message = "Le prix du produit doit être un nombre positif")
    private Double prixUnite;

    @NotNull(message = "La stock est obligatoire")
    @Positive(message = "Le stock doit être positif ou nul")
    private Integer stock;
}
