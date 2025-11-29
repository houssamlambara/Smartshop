package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;

import java.util.List;

public interface PaiementService {

    PaiementResponseDTO createPaiement(PaiementRequestDTO dto);

    List<PaiementResponseDTO> getPaiementByOrder(String orderId);

    PaiementResponseDTO validatePaiement(String paiementId);

    PaiementResponseDTO refusePaiement(String paiementId);
}
