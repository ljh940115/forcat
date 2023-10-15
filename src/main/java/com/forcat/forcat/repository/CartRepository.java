package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember_Email (String email);

    Cart findByMember_Mid (String mid);
}
