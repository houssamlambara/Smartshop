package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, String> {
    Optional<User> findByUsername(String username);

}
