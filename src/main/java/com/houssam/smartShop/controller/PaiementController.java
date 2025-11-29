package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> createPaiement(@Valid @RequestBody PaiementRequestDTO dto){
        PaiementResponseDTO response = paiementService.createPaiement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Paiement créé avec succès", response));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaiementResponseDTO>>>getPaiementByOrder(@PathVariable String orderId){
        List<PaiementResponseDTO> response = paiementService.getPaiementByOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>("Liste des paiements récupérée avec succès", response));
    }

    @PatchMapping("/{id}/validate")
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> validatePaiement(@PathVariable String id){
        PaiementResponseDTO response = paiementService.validatePaiement(id);
        return ResponseEntity.ok(new ApiResponse<>("Paiement validé avec succès", response));
    }

    @PatchMapping("/{id}/refuse")
    public ResponseEntity<ApiResponse<PaiementResponseDTO>> rejectPaiement(@PathVariable String id){
        PaiementResponseDTO response = paiementService.refusePaiement(id);
        return ResponseEntity.ok(new ApiResponse<>("Paiement rejeté avec succès", response));
    }

}
