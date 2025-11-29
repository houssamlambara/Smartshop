package com.houssam.smartShop.service.implementation;

import com.houssam.smartShop.dto.requestDTO.PaiementRequestDTO;
import com.houssam.smartShop.dto.responseDTO.PaiementResponseDTO;
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
                .orElseThrow(()-> new RuntimeException("Order not found"));

        if(dto.getTypePaiement() == PaiementMethod.ESPECE && dto.getMontant() > 2000){
            throw new RuntimeException("Montant trop élevé pour le paiement en espèces");
        }

        List<Paiement> PaiementsExistants = paiementRepository.findByOrderId(dto.getOrderId());
        double totalPaiments = PaiementsExistants
                .stream()
                .filter(p -> p.getStatus() == PaiementStatus.REJETE)
                .mapToDouble(Paiement::getMontant)
                .sum();
        if (totalPaiments + dto.getMontant() > order.getTotalTTC()){
            throw new RuntimeException("Le montant total des paiements dépasse le total de la commande");
        }

        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setOrder(order);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setNumeroPaiement(PaiementsExistants.size() +1);

        if (dto.getTypePaiement() == PaiementMethod.ESPECE){
            paiement.setStatus(PaiementStatus.ENCAISSE);
        }else {
            paiement.setStatus(PaiementStatus.EN_ATTENTE);
        }
        paiement = paiementRepository.save(paiement);

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
        paiementRepository.save(paiement);

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
