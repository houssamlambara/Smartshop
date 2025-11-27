package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.OrderItemRequestDTO;
import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.mapper.OrderItemMapper;
import com.houssam.smartShop.mapper.OrderMapper;
import com.houssam.smartShop.model.*;
import com.houssam.smartShop.repository.*;
import com.houssam.smartShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(()-> new RuntimeException("Client non trouvé"));
        PromoCode promoCode = null;
        if(dto.getPromoCode() != null && !dto.getPromoCode().isEmpty()) {
            promoCode = promoCodeRepository.findById(dto.getPromoCode()).orElse(null);
        }

        Order order = orderMapper.toEntity(dto);
        order.setDateCommande(LocalDate.now());
        order.setStatut(OrderStatus.EN_ATTENTE);
        order.setClient(client);
        order.setPromoCode(promoCode);

//        order = orderRepository.save(order);

        double sousTotal = 0.0;
        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Produit produit = produitRepository.findById(itemDTO.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            if(produit.getStock() < itemDTO.getQuantite()){
                order.setStatut(OrderStatus.REJETE);
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }

            double prixUnitaire = produit.getPrixUnite();
            double totalHT = prixUnitaire * itemDTO.getQuantite();

            OrderItem orderItem = OrderItem.builder()
                    .produit(produit)
                    .quantite(itemDTO.getQuantite())
                    .prixUnitaire(prixUnitaire)
                    .total(totalHT)
                    .order(order)
                    .build();

            order.getItems().add(orderItem);
            sousTotal += totalHT;
        }

        double remise = calculerFideliter(client, sousTotal);
        if(dto.getPromoCode() != null && dto.getPromoCode().matches("PROMO\\d+")){
            remise += sousTotal *0.5;
        }

        double montantHT = sousTotal - remise;
        double tauxTVA = montantHT * 0.20;
        double montantTVA = montantHT * tauxTVA;
        double totalTTC = montantHT + tauxTVA;

        order.setSousTotal(sousTotal);
        order.setMontantRemise(remise);
        order.setMontantHT(montantHT);
        order.setMontantTVA(montantTVA);
//        order.setMontantTVA(order.getMontantTVA());
        order.setTauxTVA(tauxTVA);
        order.setTotalTTC(totalTTC);
        orderRepository.save(order);

        client.setTotalOrders(client.getTotalOrders()+1);

        return orderMapper.toResponse(order);
    }

    private double calculerFideliter(Client client, double sousTotal){
        switch (client.getCustomerTier()){
            case SILVER -> {
                return sousTotal > 500 ? sousTotal * 0.05 :0.0;
            }
            case GOLD -> {
                return sousTotal > 800 ? sousTotal * 0.10 : 0.0;
            }
            case PLATINIUM -> {
                return sousTotal >=1200 ? sousTotal * 0.15 : 0.0;
            }
            default -> {
                return 0.0;
            }
        }
    }

    public OrderResponseDTO getOrderById(String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Commande non trouvé"));
                return orderMapper.toResponse(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrder(String id, OrderRequestDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Transactional
    public void cancelOrder(String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Commande non trouvé"));
        order.setStatut(OrderStatus.ANNULE);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(String id,OrderStatus status){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Commande non trouvé"));
        order.setStatut(status);
        orderRepository.save(order);
    }

}
