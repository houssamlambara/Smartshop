package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository <Client, String> {
    Optional<Client> findByUserId(String userId);

}
