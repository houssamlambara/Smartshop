package com.houssam.smartShop.mapper;

import com.houssam.smartShop.dto.requestDTO.OrderItemRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderItemResponseDTO;
import com.houssam.smartShop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produit", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "prixUnitaire", ignore = true)
    @Mapping(target = "total", ignore = true)
    OrderItem toEntity(OrderItemRequestDTO dto);

    @Mapping(target = "produitId", source = "produit.id")
    @Mapping(target = "produitNom", source = "produit.nom")
    OrderItemResponseDTO toResponse(OrderItem item);

}
