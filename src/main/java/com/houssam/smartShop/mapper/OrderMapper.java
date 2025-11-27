package com.houssam.smartShop.mapper;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "promoCode", ignore = true)
    @Mapping(target = "sousTotal", ignore = true)
    @Mapping(target = "montantRemise", ignore = true)
    @Mapping(target = "montantHT", ignore = true)
    @Mapping(target = "tauxTVA", ignore = true)
    @Mapping(target = "montantTVA", ignore = true)
    @Mapping(target = "totalTTC", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "dateCommande", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "promoCodeId", source = "promoCode.id")
    OrderResponseDTO toResponse(Order order);

}
