package com.houssam.smartShop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private String id;
    private String produitId;
    private String produitNom;
    private Integer quantite;
    private Double prixUnitaire;
    private Double total;
}
