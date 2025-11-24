package com.houssam.smartShop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitResponseDTO {
    private String id;
    private String nom;
    private String description;
    private BigDecimal prixUnite;
    private Integer stock;
    private Boolean delete;
}
