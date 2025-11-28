package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode,String> {
    Optional<PromoCode> findByCode(String code);

}
