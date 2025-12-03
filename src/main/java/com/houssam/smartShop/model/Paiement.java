package com.houssam.smartShop.model;

import com.houssam.smartShop.enums.PaiementMethod;
import com.houssam.smartShop.enums.PaiementStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer numeroPaiement;
    private Double montant;

    @Enumerated(EnumType.STRING)
    private PaiementMethod typePaiement;

    @Enumerated(EnumType.STRING)
    private PaiementStatus status;

    private LocalDateTime datePaiement;
    private String reference;
    private String banque;
    private LocalDateTime dateEncaissement;
    private LocalDateTime dateEcheance;
//    private String commentaire;

    @CreationTimestamp
    private  LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

}
