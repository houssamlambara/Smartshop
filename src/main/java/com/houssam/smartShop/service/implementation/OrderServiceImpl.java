package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.OrderItemRequestDTO;
import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.CustomerTier;
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
import java.time.LocalDateTime;
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
            promoCode = promoCodeRepository.findByCode(dto.getPromoCode()).orElse(null);
        }

        Order order = orderMapper.toEntity(dto);
        order.setDateCommande(LocalDate.now());
        order.setStatut(OrderStatus.EN_ATTENTE);
        order.setClient(client);
        order.setPromoCode(promoCode);

//        order = orderRepository.save(order);

        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Produit produit = produitRepository.findById(itemDTO.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            if(produit.getStock() < itemDTO.getQuantite()){
                order.setStatut(OrderStatus.REJETE);
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }

            OrderItem orderItem = OrderItem.builder()
                    .produit(produit)
                    .quantite(itemDTO.getQuantite())
                    .prixUnitaire(produit.getPrixUnite())
                    .total(produit.getPrixUnite() * itemDTO.getQuantite())
                    .order(order)
                    .build();

            order.getItems().add(orderItem);
//            sousTotal += totalHT;
        }

//        client.setCustomerTier(calculerNiveauFidelite(client));

        calculerMontants(order, client, promoCode);

        orderRepository.save(order);

        double montantCommande = order.getMontantHT();
        client.setTotalOrders(client.getTotalOrders()+1);
        client.setTotalSpent((client.getTotalSpent() != null ? client.getTotalSpent() : 0) + montantCommande);
        client.setCustomerTier(calculerNiveauFidelite(client));

        clientRepository.save(client);

        return orderMapper.toResponse(order);
    }

    private CustomerTier calculerNiveauFidelite(Client client) {
        int totalCommandes = client.getTotalOrders();
        double totalCumul = client.getTotalSpent();

        if (totalCommandes >= 20 || totalCumul >= 15000) {
            return CustomerTier.PLATINIUM;
        } else if (totalCommandes >= 10 || totalCumul >= 5000) {
            return CustomerTier.GOLD;
        } else if (totalCommandes >= 3 || totalCumul >= 1000) {
            return CustomerTier.SILVER;
        } else {
            return CustomerTier.BASIC;
        }
    }

    private double calculerFideliter(Client client, double sousTotal) {
        if (client.getCustomerTier() == null) return 0.0;

        return switch (client.getCustomerTier()) {
            case SILVER -> sousTotal >= 500 ? sousTotal * 0.05 : 0.0;
            case GOLD -> sousTotal >= 800 ? sousTotal * 0.10 : 0.0;
            case PLATINIUM -> sousTotal >= 1200 ? sousTotal * 0.15 : 0.0;
            default -> 0.0;
        };
    }

    private void calculerMontants(Order order, Client client, PromoCode promoCode){
        double sousTotal = 0.0;
        for(OrderItem item : order.getItems()){
            sousTotal += item.getTotal();
        }

        double remiseFidelite = calculerFideliter(client, sousTotal);

        double remisePromo = 0.0;
        if(promoCode != null && Boolean.TRUE.equals(promoCode.getActive())
                && (promoCode.getDateDebut() == null || promoCode.getDateDebut().isBefore(LocalDateTime.now()))
                && (promoCode.getDateFin() == null || promoCode.getDateFin().isAfter(LocalDateTime.now()))) {
            remisePromo = sousTotal * (promoCode.getDiscountPercentage() / 100);
        }

        double totalRemise = remiseFidelite + remisePromo;

        double montantHT = sousTotal - totalRemise;
        double tauxTVA = 0.20;
        double montantTVA = montantHT * tauxTVA;
        double totalTTC = montantHT + montantTVA;

        order.setSousTotal(sousTotal);
        order.setMontantRemise(totalRemise);
        order.setMontantHT(montantHT);
        order.setTauxTVA(tauxTVA);
        order.setMontantTVA(montantTVA);
        order.setTotalTTC(totalTTC);
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
