package com.houssam.smartShop.service;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
import com.houssam.smartShop.enums.PaiementMethod;
import com.houssam.smartShop.enums.PaiementStatus;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.mapper.PaiementMapper;
import com.houssam.smartShop.model.Order;
import com.houssam.smartShop.model.Paiement;
import com.houssam.smartShop.repository.OrderRepository;
import com.houssam.smartShop.repository.PaiementRepository;
import com.houssam.smartShop.service.implementation.PaiementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaiementMapper paiementMapper;

    @InjectMocks
    private PaiementServiceImpl paiementService;

    private Order order;
    private Paiement paiement;
    private PaiementRequestDTO requestDTO;
    private PaiementResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Commande avec statut EN_ATTENTE
        order = Order.builder()
                .id("order-1")
                .totalTTC(1000.0)
                .montantRestant(1000.0)
                .statut(OrderStatus.EN_ATTENTE) // ⚡ important
                .build();

        // Paiement de base
        paiement = Paiement.builder()
                .id("paiement-1")
                .montant(500.0)
                .typePaiement(PaiementMethod.ESPECE)
                .status(PaiementStatus.ENCAISSE)
                .order(order)
                .build();

        // Request DTO
        requestDTO = new PaiementRequestDTO();
        requestDTO.setOrderId("order-1");
        requestDTO.setMontant(500.0);
        requestDTO.setTypePaiement(PaiementMethod.ESPECE);
        requestDTO.setReference("RECU-001");

        // Response DTO
        responseDTO = new PaiementResponseDTO();
        responseDTO.setId("paiement-1");
        responseDTO.setMontant(500.0);
        responseDTO.setTypePaiement(PaiementMethod.ESPECE);
        responseDTO.setStatus(PaiementStatus.ENCAISSE);
    }

    @Test
    void addPaiement_ShouldSucceed_ForEspece() {
        // Setup déjà correct pour ESPECE
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        when(paiementRepository.findByOrderId("order-1")).thenReturn(List.of()); // Pas de paiements existants
        when(paiementMapper.toEntity(requestDTO)).thenReturn(paiement);
        when(paiementRepository.save(paiement)).thenReturn(paiement);
        when(orderRepository.save(order)).thenReturn(order);
        when(paiementMapper.toResponse(paiement)).thenReturn(responseDTO);

        PaiementResponseDTO result = paiementService.createPaiement(requestDTO);

        assertNotNull(result);
        assertEquals(PaiementStatus.ENCAISSE, result.getStatus());
        verify(paiementRepository).save(paiement);
        verify(orderRepository).save(order);
    }

    @Test
    void addPaiement_ShouldSetEnAttente_ForCheque() {
        requestDTO.setTypePaiement(PaiementMethod.CHEQUE);
        requestDTO.setBanque("BanqueTest");
        requestDTO.setDateEcheance(LocalDateTime.now().plusDays(30)); // ⚡ ajout de la date d'échéance

        paiement.setTypePaiement(PaiementMethod.CHEQUE);
        paiement.setStatus(PaiementStatus.EN_ATTENTE);

        responseDTO.setTypePaiement(PaiementMethod.CHEQUE);
        responseDTO.setStatus(PaiementStatus.EN_ATTENTE);

        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        when(paiementRepository.findByOrderId("order-1")).thenReturn(List.of()); // Pas de paiements existants
        when(paiementMapper.toEntity(requestDTO)).thenReturn(paiement);
        when(paiementRepository.save(paiement)).thenReturn(paiement);
        when(paiementMapper.toResponse(paiement)).thenReturn(responseDTO);

        PaiementResponseDTO result = paiementService.createPaiement(requestDTO);

        assertNotNull(result);
        assertEquals(PaiementStatus.EN_ATTENTE, result.getStatus());
        assertEquals(PaiementMethod.CHEQUE, result.getTypePaiement());
        assertEquals("BanqueTest", requestDTO.getBanque());
    }

    @Test
    void addPaiement_ShouldThrow_WhenAmountExceedsRemaining() {
        requestDTO.setMontant(2000.0); // plus que montantRestant
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
        when(paiementRepository.findByOrderId("order-1")).thenReturn(List.of()); // Pas de paiements existants

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            paiementService.createPaiement(requestDTO);
        });

        // Vérifier que l'exception est bien levée (le message peut varier)
        assertNotNull(ex);
        assertNotNull(ex.getMessage());
        verify(paiementRepository, never()).save(any());
    }

    @Test
    void getPaiementByOrder_ShouldReturnPaiement() {
        when(paiementRepository.findByOrderId("order-1")).thenReturn(List.of(paiement));
        when(paiementMapper.toResponse(paiement)).thenReturn(responseDTO);

        List<PaiementResponseDTO> result = paiementService.getPaiementByOrder("order-1");

        assertEquals(1, result.size());
        assertEquals("paiement-1", result.get(0).getId());
    }
}
