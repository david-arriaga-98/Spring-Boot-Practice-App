package com.isac.ecommerce.repositories;

import com.isac.ecommerce.models.Cart;
import com.isac.ecommerce.models.Order;
import com.isac.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByProductAndOrder(Product product, Order order);



}