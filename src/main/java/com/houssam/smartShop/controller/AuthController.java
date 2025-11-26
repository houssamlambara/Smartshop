package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.LoginRequestDTO;
import com.houssam.smartShop.dto.responseDTO.LoginResponseDTO;
import com.houssam.smartShop.service.implementation.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request) {

        LoginResponseDTO response = authServiceImpl.login(loginRequest, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authServiceImpl.logout(request);
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/session")
    public ResponseEntity<LoginResponseDTO> getCurrentUser(HttpServletRequest request) {
        LoginResponseDTO response = authServiceImpl.getCurrentUser(request);
        return ResponseEntity.ok(response);
    }
}

