package com.houssam.smartShop.mapper;

import com.houssam.smartShop.dto.requestDTO.UserRequestDTO;
import com.houssam.smartShop.dto.responseDTO.UserResponseDTO;
import com.houssam.smartShop.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponse(User user);
    User toEntity(UserRequestDTO dto);
}
