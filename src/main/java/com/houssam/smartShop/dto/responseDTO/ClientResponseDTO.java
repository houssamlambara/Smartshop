package com.houssam.smartShop.dto.responseDTO;

import com.houssam.smartShop.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private CustomerTier customerTier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
    private String userId;

}
