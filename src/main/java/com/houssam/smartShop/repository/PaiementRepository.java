package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository <Paiement, String> {
    List<Paiement> findByOrderId(String orderId);
}
