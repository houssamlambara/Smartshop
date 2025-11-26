package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.ProduitRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ProduitResponseDTO;
import com.houssam.smartShop.mapper.ProduitMapper;
import com.houssam.smartShop.model.Produit;
import com.houssam.smartShop.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    @Transactional
    public ProduitResponseDTO createProduit(ProduitRequestDTO dto){
        Produit produit = produitMapper.toEntity(dto);
        produit.setDelete(false);
        produit = produitRepository.save(produit);
        return produitMapper.toResponse(produit);
    }

    public ProduitResponseDTO getProduitById(String id){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Produit non trouver avec ID:"+ id));
        return produitMapper.toResponse(produit);
    }

    public List<ProduitResponseDTO> getAllProduits(){
        return produitRepository.findAll()
                .stream()
                .filter(produit -> !produit.getDelete())
                .map(produitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProduitResponseDTO updateProduit(String id, ProduitRequestDTO dto){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Produit non trouver avec ce Id:" +id));

        if (produit.getDelete()){
            throw new RuntimeException("Impossible de modifier un produit supprimer");
        }

        produitMapper.updateEntity(dto, produit);
        produit = produitRepository.save(produit);
        return produitMapper.toResponse(produit);
    }

    @Transactional
    public void deleteProduit(String id){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Produit non trouver avec ce Id:" +id));
        produit.setDelete(true);
        produitRepository.save(produit);
    }
}
