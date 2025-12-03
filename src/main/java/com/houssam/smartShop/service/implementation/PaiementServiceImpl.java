package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
import com.houssam.smartShop.enums.OrderStatus;
import com.houssam.smartShop.enums.PaiementMethod;
import com.houssam.smartShop.enums.PaiementStatus;
import com.houssam.smartShop.mapper.PaiementMapper;
import com.houssam.smartShop.model.Order;
import com.houssam.smartShop.model.Paiement;
import com.houssam.smartShop.repository.OrderRepository;
import com.houssam.smartShop.repository.PaiementRepository;
import com.houssam.smartShop.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaiementResponseDTO createPaiement(PaiementRequestDTO dto){
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(()-> new RuntimeException("Commande non trouvée avec ID:" + dto.getOrderId()));

        if (order.getStatut() != OrderStatus.EN_ATTENTE) {
            throw new RuntimeException("Impossible d'ajouter un paiement : la commande n'est pas en attente (statut actuel : " + order.getStatut() + ")");
        }

        if (dto.getMontant() == null || dto.getMontant() <= 0) {
            throw new RuntimeException("Le montant du paiement doit être supérieur à 0");
        }

        if(dto.getTypePaiement() == PaiementMethod.ESPECE && dto.getMontant() > 20000){
            throw new RuntimeException("Limite légale dépassée : paiement en espèces limité à 20,000 DH");
        }

        List<Paiement> paiementsExistants = paiementRepository.findByOrderId(dto.getOrderId());
        double totalPaiementsEncaisses = paiementsExistants
                .stream()
                .filter(p -> p.getStatus() == PaiementStatus.ENCAISSE)
                .mapToDouble(Paiement::getMontant)
                .sum();

        if (dto.getMontant() > order.getMontantRestant()){
            throw new RuntimeException(String.format(
                "Montant trop élevé : il reste %.2f DH à payer sur cette commande",
                order.getMontantRestant()
            ));
        }

        if (dto.getTypePaiement() == PaiementMethod.CHEQUE) {
            if (dto.getReference() == null || dto.getReference().trim().isEmpty()) {
                throw new RuntimeException("Le numéro de chèque est obligatoire pour un paiement par CHEQUE");
            }
            if (dto.getBanque() == null || dto.getBanque().trim().isEmpty()) {
                throw new RuntimeException("La banque est obligatoire pour un paiement par CHEQUE");
            }
            if (dto.getDateEcheance() == null) {
                throw new RuntimeException("La date d'échéance est obligatoire pour un paiement par CHEQUE");
            }
        }

        if (dto.getTypePaiement() == PaiementMethod.VIREMENT) {
            if (dto.getReference() == null || dto.getReference().trim().isEmpty()) {
                throw new RuntimeException("La référence de virement est obligatoire pour un paiement par VIREMENT");
            }
            if (dto.getBanque() == null || dto.getBanque().trim().isEmpty()) {
                throw new RuntimeException("La banque est obligatoire pour un paiement par VIREMENT");
            }
        }

        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setOrder(order);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setNumeroPaiement(paiementsExistants.size() + 1);

        if (dto.getTypePaiement() == PaiementMethod.ESPECE){
            paiement.setStatus(PaiementStatus.ENCAISSE);
            paiement.setDateEncaissement(LocalDateTime.now());
        } else {
            paiement.setStatus(PaiementStatus.EN_ATTENTE);
        }

        paiement = paiementRepository.save(paiement);

        if (paiement.getStatus() == PaiementStatus.ENCAISSE) {
            double nouveauMontantRestant = order.getMontantRestant() - dto.getMontant();
            order.setMontantRestant(Math.max(0, nouveauMontantRestant));
            orderRepository.save(order);
        }

        return paiementMapper.toResponse(paiement);
    }

    @Override
    public List<PaiementResponseDTO> getPaiementByOrder(String orderId) {
        return paiementRepository.findByOrderId(orderId).stream()
                .map(paiementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaiementResponseDTO validatePaiement(String id){
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Paiement non trouvé"));

        if (paiement.getStatus() == PaiementStatus.ENCAISSE){
            throw new RuntimeException("Paiement déjà encaissé");
        }

        paiement.setStatus(PaiementStatus.ENCAISSE);
        paiement.setDateEncaissement(LocalDateTime.now());
        paiementRepository.save(paiement);
        Order order = paiement.getOrder();
        double nouveauMontantRestant = order.getMontantRestant() - paiement.getMontant();
        order.setMontantRestant(Math.max(0, nouveauMontantRestant));
        orderRepository.save(order);

        return paiementMapper.toResponse(paiement);
    }

    @Override
    @Transactional
    public PaiementResponseDTO refusePaiement(String id){
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Paiement non trouvé"));
        paiement.setStatus(PaiementStatus.REJETE);
        return paiementMapper.toResponse(paiementRepository.save(paiement));
    }
}
