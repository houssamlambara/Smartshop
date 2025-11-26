package com.houssam.smartShop.controller;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.response.ApiResponse;
import com.houssam.smartShop.service.implementation.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderServiceImpl.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Commande créée avec succès", order));
    }

}