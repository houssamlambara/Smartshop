package com.houssam.smartShop.dto.responseDTO;

import com.houssam.smartShop.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String userId;
    private String username;
    private UserRole role;
    private String message;
}
