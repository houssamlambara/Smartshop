package com.houssam.smartShop.mapper;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
import com.houssam.smartShop.model.Paiement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaiementMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaiementResponseDTO toResponse(Paiement paiement);

    Paiement toEntity(PaiementRequestDTO requestDTO);
}
