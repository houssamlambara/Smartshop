package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.ClientRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ClientResponseDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.implementation.ClientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients (admin et profil client)")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @PostMapping("/admin/clients")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientServiceImpl.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Client cree avec succes", client));
    }

    @Operation(summary = "Lister tous les clients", description = "Récupère la liste paginée de tous les clients (Admin uniquement)")
    @GetMapping("/admin/clients")
    public ResponseEntity<ApiResponse<Page<ClientResponseDTO>>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        Page<ClientResponseDTO> clients = clientServiceImpl.getAllClients(page, size);
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients", clients));
    }

    @Operation(summary = "Obtenir un client par ID", description = "Récupère les détails d'un client spécifique (Admin uniquement)")
    @GetMapping("/admin/clients/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>>getClientById(@PathVariable String id){
        ClientResponseDTO client = clientServiceImpl.getClientById(id);
        return ResponseEntity.ok(new ApiResponse<>("Details du client", client));   }

    @Operation(summary = "Mettre à jour un client", description = "Modifie les informations d'un client existant (Admin uniquement)")
    @PutMapping("/admin/clients/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> updateClient(@PathVariable String id, @Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientServiceImpl.updateClient(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Client mis a jour avec succes", client));
    }

    @Operation(summary = "Supprimer un client", description = "Supprime un client du système (Admin uniquement)")
    @DeleteMapping("/admin/clients/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String id){
        clientServiceImpl.deleteClient(id);
        return ResponseEntity.ok (new ApiResponse<>("Client supprime avec succes", null));
    }

    @Operation(summary = "Obtenir les commandes d'un client", description = "Récupère toutes les commandes d'un client spécifique (Admin uniquement)")
    @GetMapping("/admin/clients/{id}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getClientOrders(@PathVariable String id){
        List<OrderResponseDTO> orders = clientServiceImpl.getClientOrders(id);
        return ResponseEntity.ok(new ApiResponse<>("Liste des commandes du client", orders));
    }

    @Operation(summary = "Obtenir mon profil", description = "Récupère le profil du client connecté")
    @GetMapping("/client/profile")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> getClientProfile(HttpSession session){
        ClientResponseDTO client = clientServiceImpl.getMyProfile(session);
        return ResponseEntity.ok(new ApiResponse<>("Profile du client", client));
    }

    @Operation(summary = "Mettre à jour mon profil", description = "Modifie les informations du profil du client connecté")
    @PutMapping("/client/profile/update")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> updateClientProfile(HttpSession session, @Valid @RequestBody ClientRequestDTO dto){
        ClientResponseDTO client = clientServiceImpl.updateMyProfile(dto, session);
        return ResponseEntity.ok(new ApiResponse<>("Profile du client mis a jour avec succes", client));
    }

    @Operation(summary = "Obtenir mes commandes", description = "Récupère toutes les commandes du client connecté")
    @GetMapping("/client/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getMyOrders(HttpSession session){
        List<OrderResponseDTO> orders = clientServiceImpl.getMyOrders(session);
        return ResponseEntity.ok(new ApiResponse<>("Vos commandes", orders));
    }

}
