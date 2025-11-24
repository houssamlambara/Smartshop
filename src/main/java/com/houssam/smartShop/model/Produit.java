package com.houssam.smartShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;
    private String nom;
    private String description;
    private BigDecimal prixUnite;
    private Integer stock;
    private Boolean delete = false;

    @OneToMany(mappedBy = "produit")
    private List<OrderItem> orderItems;
}
