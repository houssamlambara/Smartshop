package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.PaiementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/paiements")
@RestController
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Gestion des paiements (Admin uniquement)")
public class PaiementController {
    private final PaiementService paiementService;

    @Operation(summary = "Créer un paiement", description = "Enregistre un nouveau paiement pour une commande")
    @PostMapping
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> createPaiement(@Valid @RequestBody PaiementRequestDTO dto){
        PaiementResponseDTO response = paiementService.createPaiement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Paiement créé avec succès", response));
    }

    @Operation(summary = "Obtenir les paiements d'une commande", description = "Récupère tous les paiements associés à une commande")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaiementResponseDTO>>>getPaiementByOrder(@PathVariable String orderId){
        List<PaiementResponseDTO> response = paiementService.getPaiementByOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>("Liste des paiements récupérée avec succès", response));
    }

    @Operation(summary = "Valider un paiement", description = "Valide un paiement en attente")
    @PatchMapping("/{id}/validate")
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> validatePaiement(@PathVariable String id){
        PaiementResponseDTO response = paiementService.validatePaiement(id);
        return ResponseEntity.ok(new ApiResponse<>("Paiement validé avec succès", response));
    }

    @Operation(summary = "Refuser un paiement", description = "Rejette un paiement en attente")
    @PatchMapping("/{id}/refuse")
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> rejectPaiement(@PathVariable String id){
        PaiementResponseDTO response = paiementService.refusePaiement(id);
        return ResponseEntity.ok(new ApiResponse<>("Paiement rejeté avec succès", response));
    }

}
