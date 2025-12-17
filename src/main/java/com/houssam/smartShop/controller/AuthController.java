package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.LoginRequestDTO;
import com.houssam.smartShop.dto.responseDTO.LoginResponseDTO;
import com.houssam.smartShop.service.implementation.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Gestion de l'authentification des utilisateurs")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur avec email et mot de passe")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request) {

        LoginResponseDTO response = authServiceImpl.login(loginRequest, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Déconnexion", description = "Déconnecte l'utilisateur et invalide sa session")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authServiceImpl.logout(request);
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @Operation(summary = "Obtenir l'utilisateur connecté", description = "Récupère les informations de l'utilisateur actuellement connecté")
    @GetMapping("/session")
    public ResponseEntity<LoginResponseDTO> getCurrentUser(HttpServletRequest request) {
        LoginResponseDTO response = authServiceImpl.getCurrentUser(request);
        return ResponseEntity.ok(response);
    }
}

