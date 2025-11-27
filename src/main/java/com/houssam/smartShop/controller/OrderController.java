package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Commande créée avec succès", order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(){
    List<OrderResponseDTO> orders = orderService.getAllOrders();
    return ResponseEntity.ok(new ApiResponse<>("Liste des commandes récupérée avec succès", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable String id){
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande récupérée avec succès", order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrder(@PathVariable String id, @Valid @RequestBody OrderRequestDTO dto){
        OrderResponseDTO order = orderService.updateOrder(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Commande mise à jour avec succès", order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable String id, @RequestParam OrderStatus status){
        orderService.updateOrderStatus(id,status);
        return ResponseEntity.ok(new ApiResponse<>("Statut de la commande mis à jour avec succès", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String id){
        orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande annulée avec succès", null));
    }
}