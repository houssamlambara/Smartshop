package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Commandes", description = "Gestion des commandes clients")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Commande créée avec succès", order));
    }

    @Operation(summary = "Lister toutes les commandes", description = "Récupère la liste de toutes les commandes (Admin uniquement)")
    @GetMapping("/admin/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(){
    List<OrderResponseDTO> orders = orderService.getAllOrders();
    return ResponseEntity.ok(new ApiResponse<>("Liste des commandes récupérée avec succès", orders));
    }

    @Operation(summary = "Obtenir une commande par ID", description = "Récupère les détails d'une commande spécifique (Admin uniquement)")
    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable String id){
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande récupérée avec succès", order));
    }

    @Operation(summary = "Mettre à jour une commande", description = "Modifie les informations d'une commande existante (Admin uniquement)")
    @PutMapping("/admin/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrder(@PathVariable String id, @Valid @RequestBody OrderRequestDTO dto){
        OrderResponseDTO order = orderService.updateOrder(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Commande mise à jour avec succès", order));
    }

    @Operation(summary = "Mettre à jour le statut d'une commande", description = "Change le statut d'une commande (Admin uniquement)")
    @PatchMapping("/admin/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable String id, @RequestParam OrderStatus status){
        orderService.updateOrderStatus(id,status);
        return ResponseEntity.ok(new ApiResponse<>("Statut de la commande mis à jour avec succès", null));
    }

    @Operation(summary = "Annuler une commande", description = "Annule une commande existante (Admin uniquement)")
    @DeleteMapping("/admin/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String id){
        orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande annulée avec succès", null));
    }

    @Operation(summary = "Confirmer une commande", description = "Confirme une commande en attente (Admin uniquement)")
    @PatchMapping("/admin/orders/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> confirmOrder(@PathVariable String id) {
        OrderResponseDTO order = orderService.confirmOrder(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande confirmée avec succès", order));
    }

}