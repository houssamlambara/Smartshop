package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.ProduitRequestDTO;
import com.houssam.smartShop.dto.responseDTO.ProduitResponseDTO;
import com.houssam.smartShop.enums.UserRole;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.implementation.ProduitServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api") // préfixe commun
@RestController
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitServiceImpl produitServiceImpl;

    @PostMapping("/admin/produits")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> createProduit(@Valid @RequestBody ProduitRequestDTO dto, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        UserRole role = (UserRole) session.getAttribute("role");
        if (role == null || role != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Acces refusé : ADMIN requis", null));
        }
        ProduitResponseDTO response = produitServiceImpl.createProduit(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Produit créé avec succès", response));
    }

    @GetMapping("/client/produits/{id}")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> getProduitById(@PathVariable String id){
        ProduitResponseDTO response = produitServiceImpl.getProduitById(id);
        return ResponseEntity.ok(new ApiResponse<>("Produit recupere avec succes", response));
    }

    @GetMapping("/client/produits")
    public ResponseEntity<ApiResponse<List<ProduitResponseDTO>>> getAllProduits(){
        List<ProduitResponseDTO> response = produitServiceImpl.getAllProduits();
        return ResponseEntity.ok(new ApiResponse<>("Liste des produits", response));
    }

    @PutMapping("/admin/produits/{id}")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> updateProduit(@PathVariable String id, @Valid @RequestBody ProduitRequestDTO dto, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        UserRole role = (UserRole) session.getAttribute("role");
        if (role == null || role != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Acces refusé : ADMIN requis", null));
        }
        ProduitResponseDTO response = produitServiceImpl.updateProduit(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Produit mis a jour avec succes",response));
    }

    @DeleteMapping("/admin/produits/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(@PathVariable String id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        UserRole role = (UserRole) session.getAttribute("role");
        if (role == null || role != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Acces refusé : ADMIN requis", null));
        }
        produitServiceImpl.deleteProduit(id);
        return ResponseEntity.ok(new ApiResponse<>("Produit supprimé avec succès", null));
    }
}
