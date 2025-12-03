package com.houssam.smartShop.dto.requestDTO;


import com.houssam.smartShop.enums.PaiementMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaiementRequestDTO {

//    @NotNull(message = "Le mode de paiement est obligatoire")
//    private String modePaiement;

    @NotNull(message = "Le montant est obligatoire")
    @Min(value = 0, message = "Le montant doit être supérieur ou égal à 0")
    private Double montant;

    @NotNull(message = "Le type de paiement est obligatoire")
    private PaiementMethod typePaiement;

//    @NotNull(message = "Le statut du paiement est obligatoire")
//    private PaiementStatus status;

    private String reference;
    private String banque;
    private LocalDateTime dateEcheance;

//    @NotNull(message = "La date de paiement est obligatoire")
//    private LocalDateTime datePaiement;

    @NotNull(message = "L'ID de la commande est obligatoire")
    private String orderId;
}
