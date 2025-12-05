package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.CustomerTier;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.mapper.OrderMapper;
import com.houssam.smartShop.model.Client;
import com.houssam.smartShop.model.Order;
import com.houssam.smartShop.repository.ClientRepository;
import com.houssam.smartShop.repository.OrderRepository;
import com.houssam.smartShop.service.implementation.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Client client;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id("client-1")
                .customerTier(CustomerTier.SILVER)
                .totalOrders(5)
                .totalSpent(2000.0)
                .build();

        order = Order.builder()
                .id("order-1")
                .client(client)
                .statut(OrderStatus.EN_ATTENTE)
                .sousTotal(1000.0)
                .montantHT(950.0)
                .montantTVA(190.0)
                .totalTTC(1140.0)
                .montantRestant(0.0)
                .build();

        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId("order-1");
        orderResponseDTO.setStatut(OrderStatus.CONFIRME);
    }

    @Test
    void confirmOrder_ShouldUpdateOrderStatus() {
        // Arrange
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.confirmOrder("order-1");

        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRME, result.getStatut());
        verify(orderRepository, times(1)).findById("order-1");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder() {

        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.getOrderById("order-1");

        assertNotNull(result);
        assertEquals("order-1", result.getId());
        verify(orderRepository, times(1)).findById("order-1");
    }

    @Test
    void cancelOrder_ShouldUpdateOrderStatus() {

        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        orderService.cancelOrder("order-1");
        verify(orderRepository, times(1)).findById("order-1");
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}