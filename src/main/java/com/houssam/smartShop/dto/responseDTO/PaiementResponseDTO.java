package com.houssam.smartShop.dto.responseDTO;

import com.houssam.smartShop.enums.PaiementMethod;
import com.houssam.smartShop.enums.PaiementStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaiementResponseDTO {

    private String id;
    private Integer numeroPaiement;
    private Double montant;
    private PaiementMethod typePaiement;
    private String reference;
    private String banque;
    private PaiementStatus status;
    private LocalDate dateEcheance;
    private LocalDateTime dateEncaissement;
    private LocalDateTime datePaiement;
    private LocalDateTime createdAt;
    private String orderId;
}
