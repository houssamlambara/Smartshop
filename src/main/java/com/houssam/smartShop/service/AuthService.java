package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.LoginRequestDTO;
import com.houssam.smartShop.dto.responseDTO.LoginResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest);

    void logout(HttpServletRequest request);

    LoginResponseDTO getCurrentUser(HttpServletRequest request);

}
