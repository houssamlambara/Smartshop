package com.houssam.smartShop.mapper;

import com.houssam.smartShop.dto.requestDTO.ProduitRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ProduitResponseDTO;
import com.houssam.smartShop.model.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    ProduitResponseDTO toResponse(Produit ptoduit);

    Produit toEntity(ProduitRequestDTO dto);

    void updateEntity(ProduitRequestDTO dto, @MappingTarget Produit produit);
}
