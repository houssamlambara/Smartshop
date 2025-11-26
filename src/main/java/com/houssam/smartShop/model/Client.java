package com.houssam.smartShop.model;

import com.houssam.smartShop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nom;
    private String prenom;
    private String email;
    @Enumerated(EnumType.STRING)
    private CustomerTier customerTier = CustomerTier.BASIC;
    private Integer totalOrders = 0;
    private BigDecimal totalSpent = BigDecimal.ZERO;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
