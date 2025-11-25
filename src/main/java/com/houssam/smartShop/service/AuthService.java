package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.LoginRequestDTO;
import com.houssam.smartShop.dto.responseDTO.LoginResponseDTO;
import com.houssam.smartShop.exception.ResourceNotFoundException;
import com.houssam.smartShop.model.User;
import com.houssam.smartShop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest) {

        // 1. Chercher l'utilisateur par username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username ou mot de passe incorrect"));

        // 2. Vérifier le mot de passe (simple comparaison pour le brief)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Username ou mot de passe incorrect");
        }

        // 3. Créer la session HTTP
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        // 4. Retourner la réponse
        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Connexion réussie")
                .build();
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public LoginResponseDTO getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            throw new ResourceNotFoundException("Aucun utilisateur connecté");
        }

        String userId = (String) session.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Utilisateur connecté")
                .build();
    }
}
