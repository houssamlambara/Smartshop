package com.houssam.smartShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer quantite;
    private Double prixUnitaire;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable=false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable=false)
    private Order order;
}
