package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
