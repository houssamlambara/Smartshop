package com.houssam.smartShop.dto.responseDTO;

import com.houssam.smartShop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private String id;
    private LocalDate dateCommande;
    private BigDecimal sousTotal;
    private BigDecimal montantRemise;
    private BigDecimal montantHT;
    private BigDecimal tauxTVA;
    private BigDecimal montantTVA;
    private BigDecimal totalTTC;
    private OrderStatus statut;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String clientId;
    private String promoCodeId;
}
