package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ProduitRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ProduitResponseDTO;

import java.util.List;

public interface ProduitService {

    ProduitResponseDTO createProduit(ProduitRequestDTO dto);

    ProduitResponseDTO getProduitById(String id);

    List<ProduitResponseDTO> getAllProduits();

    ProduitResponseDTO updateProduit(String id, ProduitRequestDTO dto);

    void deleteProduit(String id);
}
