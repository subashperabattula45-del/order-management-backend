package com.subash.ordermanagement.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subash.ordermanagement.order.entity.Order;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}