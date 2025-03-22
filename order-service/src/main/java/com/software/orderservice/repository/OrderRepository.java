package com.software.orderservice.repository;

import com.software.orderservice.bean.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByproductName(String productName);
}
