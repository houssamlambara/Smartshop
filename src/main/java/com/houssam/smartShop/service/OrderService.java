package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);


    }
