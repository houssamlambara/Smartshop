package com.houssam.smartShop.repository;

import com.houssam.smartShop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
}
