package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository <Produit, String> {
}
