package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart , Short> {

    Optional<Cart> findByUser(User user);
}
