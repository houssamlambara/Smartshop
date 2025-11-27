package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    OrderResponseDTO getOrderById(String id);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO updateOrder(String id, OrderRequestDTO dto);

    void cancelOrder(String id);

    void updateOrderStatus(String id, OrderStatus status);

    }
