package com.houssam.smartShop.model;

import com.houssam.smartShop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private LocalDate dateCommande;
    private BigDecimal sousTotal;
    private BigDecimal montantRemise;
    private BigDecimal montantHT;
    private BigDecimal tauxTVA;
    private BigDecimal montantTVA;
    private BigDecimal totalTTC;
    private String codePromo;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}
