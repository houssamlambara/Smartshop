package com.houssam.smartShop.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotBlank(message = "L'ID du client est obligatoire")
    private String clientId;

    private String promoCode;

//    @NotNull(message = "La date de la commande est obligatoire")
//    private LocalDate dateCommande;
//
//    private Double sousTotal;
//    private Double montantRemise;
//    private Double montantHT;
//    private Double tauxTVA;
//    private Double montantTVA;
//    private Double totalTTC;

    @NotEmpty(message = "La commande doit contenir au moins un article")
    @Valid
    private List<OrderItemRequestDTO> items;

}
