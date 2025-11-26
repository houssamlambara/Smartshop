package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.OrderItemRequestDTO;
import com.houssam.smartShop.dto.requestDTO.OrderRequestDTO;
import com.houssam.smartShop.dto.responseDTO.OrderResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.mapper.OrderMapper;
import com.houssam.smartShop.model.*;
import com.houssam.smartShop.repository.*;
import com.houssam.smartShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(()-> new RuntimeException("Client non trouvé"));
        PromoCode promoCode = null;
        if(dto.getPromoCode() != null) {
            promoCode = promoCodeRepository.findById(dto.getPromoCode())
                    .orElse(null);
        }
        Order order = orderMapper.toEntity(dto);
        order.setClient(client);
        order.setPromoCode(promoCode);
        order.setStatut(OrderStatus.EN_ATTENTE);
        List<OrderItem> items = new ArrayList<>();
        double totalTTC = 0.0;
        for(OrderItemRequestDTO itemDTO : dto.getItems()){
            Produit produit = produitRepository.findById(itemDTO.getProduitId())
                    .orElseThrow(()-> new RuntimeException("Produit non trouvé"));
            if(produit.getStock()< itemDTO.getQuantite()){
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }
            produit.setStock(produit.getStock() - itemDTO.getQuantite());
            produitRepository.save(produit);
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduit(produit);
            item.setQuantite(itemDTO.getQuantite());
            item.setPrixUnitaire(produit.getPrixUnite());
            item.setTotal(itemDTO.getQuantite() * produit.getPrixUnite());
            items.add(item);
            totalTTC += item.getTotal();
        }
        order.setItems(items);
        order.setTotalTTC(totalTTC);
        orderRepository.save(order);
        orderItemRepository.saveAll(items);
        return orderMapper.toResponse(order);
    }
}
